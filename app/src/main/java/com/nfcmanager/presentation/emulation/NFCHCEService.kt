package com.nfcmanager.presentation.emulation

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

/**
 * Host Card Emulation Service.
 * Responds to APDU commands from NFC readers by replying with the stored card UID.
 *
 * For access control systems (turnstiles, intercoms) that simply read the UID,
 * we respond with the UID bytes to SELECT and READ commands.
 */
class NFCHCEService : HostApduService() {

    companion object {
        private const val TAG = "NFCHCEService"

        // SELECT AID command header
        private val SELECT_AID_HEADER = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)

        // GET DATA / READ RECORD
        private val GET_UID_COMMAND = byteArrayOf(0xFF.toByte(), 0xCA.toByte(), 0x00, 0x00, 0x00)

        // Success response
        private val SUCCESS_SW = byteArrayOf(0x90.toByte(), 0x00)
        private val UNKNOWN_CMD_SW = byteArrayOf(0x00, 0x00)

        // Global emulated UID shared with the service
        var emulatedUidBytes: ByteArray? = null
        var isEmulating: Boolean = false
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) return UNKNOWN_CMD_SW

        Log.d(TAG, "APDU: ${commandApdu.toHex()}")
        Log.d(TAG, "Emulating: $isEmulating, UID: ${emulatedUidBytes?.toHex()}")

        val uid = emulatedUidBytes
        if (!isEmulating || uid == null) {
            return UNKNOWN_CMD_SW
        }

        // Respond to SELECT AID
        if (commandApdu.size >= 4 &&
            commandApdu[0] == SELECT_AID_HEADER[0] &&
            commandApdu[1] == SELECT_AID_HEADER[1] &&
            commandApdu[2] == SELECT_AID_HEADER[2]
        ) {
            Log.d(TAG, "SELECT AID received, responding with UID + SW")
            return uid + SUCCESS_SW
        }

        // Respond to GET UID (FF CA 00 00 00)
        if (commandApdu.size >= 5 &&
            commandApdu[0] == GET_UID_COMMAND[0] &&
            commandApdu[1] == GET_UID_COMMAND[1] &&
            commandApdu[2] == GET_UID_COMMAND[2]
        ) {
            Log.d(TAG, "GET UID received, responding with UID: ${uid.toHex()}")
            return uid + SUCCESS_SW
        }

        // For any other command — still respond with UID so readers can detect us
        Log.d(TAG, "Unknown APDU, sending UID as fallback")
        return uid + SUCCESS_SW
    }

    override fun onDeactivated(reason: Int) {
        Log.d(TAG, "HCE deactivated, reason: $reason")
    }
}

fun ByteArray.toHex(): String = joinToString("") { "%02X".format(it) }
