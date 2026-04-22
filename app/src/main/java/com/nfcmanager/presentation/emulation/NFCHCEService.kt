package com.nfcmanager.presentation.emulation

import android.content.SharedPreferences
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

/**
 * Host Card Emulation Service — исправленная версия.
 *
 * ВАЖНО о HCE и совместимости со считывателями СКУД:
 * -------------------------------------------------------
 * HCE работает только на уровне ISO 14443-4 (IsoDep / APDU).
 * Большинство турникетов и домофонов на Mifare Classic читают UID
 * непосредственно на уровне RF-антиколлизии (ISO 14443-3),
 * ДО любого APDU-обмена. Эмулировать это через HCE невозможно
 * на стандартном Android без root + модифицированного ядра.
 *
 * HCE БУДЕТ работать со считывателями, которые:
 * - Используют ISO 14443-4 / IsoDep протокол
 * - Работают с APDU командами (SELECT AID, GET DATA и т.д.)
 * - Совместимы с EMV, NDEF или другими ISO-DEP приложениями
 *
 * UID хранится в SharedPreferences чтобы переживать перезапуск сервиса.
 */
class NFCHCEService : HostApduService() {

    companion object {
        private const val TAG = "NFCHCEService"
        const val PREFS_NAME = "nfc_hce_prefs"
        const val KEY_UID_HEX = "emulated_uid_hex"
        const val KEY_IS_EMULATING = "is_emulating"

        // SELECT AID — стандартная команда выбора приложения
        private val SELECT_AID_HEADER = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)

        // GET UID — нестандартная, но поддерживаемая многими ридерами
        private val GET_UID_COMMAND = byteArrayOf(0xFF.toByte(), 0xCA.toByte(), 0x00, 0x00, 0x00)

        // READ BINARY — ISO команда чтения данных
        private val READ_BINARY_HEADER = byteArrayOf(0x00, 0xB0.toByte())

        // GET DATA
        private val GET_DATA_HEADER = byteArrayOf(0x00, 0xCA.toByte())

        // Status words
        val SW_OK          = byteArrayOf(0x90.toByte(), 0x00)
        val SW_UNKNOWN_CMD = byteArrayOf(0x6D.toByte(), 0x00) // INS not supported
        val SW_CONDITIONS  = byteArrayOf(0x69.toByte(), 0x85.toByte()) // эмуляция не активна

        // Утилита для записи UID из ViewModel/Activity в SharedPreferences
        fun setEmulationData(prefs: SharedPreferences, uidHex: String, active: Boolean) {
            prefs.edit()
                .putString(KEY_UID_HEX, uidHex)
                .putBoolean(KEY_IS_EMULATING, active)
                .apply()
            Log.d(TAG, "Emulation data saved: uid=$uidHex active=$active")
        }
    }

    private lateinit var prefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        Log.d(TAG, "NFCHCEService created")
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null || commandApdu.isEmpty()) return SW_UNKNOWN_CMD

        Log.d(TAG, "← APDU received: ${commandApdu.toHex()}")

        // Читаем актуальное состояние из SharedPreferences каждый раз —
        // companion object сбрасывается при убийстве процесса, SharedPreferences — нет
        val isEmulating = prefs.getBoolean(KEY_IS_EMULATING, false)
        val uidHex = prefs.getString(KEY_UID_HEX, null)

        if (!isEmulating || uidHex.isNullOrBlank()) {
            Log.d(TAG, "→ Emulation inactive → SW_CONDITIONS")
            return SW_CONDITIONS
        }

        val uidBytes = uidHex.hexToByteArray()
        Log.d(TAG, "Emulating UID: $uidHex")

        val response = when {
            // SELECT AID (00 A4 04 00 ...)
            isSelectAid(commandApdu) -> {
                Log.d(TAG, "→ SELECT AID")
                uidBytes + SW_OK
            }

            // GET UID (FF CA 00 00 00)
            isGetUid(commandApdu) -> {
                Log.d(TAG, "→ GET UID")
                uidBytes + SW_OK
            }

            // READ BINARY (00 B0 ...)
            commandApdu.size >= 2 &&
            commandApdu[0] == READ_BINARY_HEADER[0] &&
            commandApdu[1] == READ_BINARY_HEADER[1] -> {
                Log.d(TAG, "→ READ BINARY")
                val le = if (commandApdu.size > 4) commandApdu[4].toInt() and 0xFF else uidBytes.size
                val buf = ByteArray(le)
                uidBytes.copyInto(buf, 0, 0, minOf(uidBytes.size, le))
                buf + SW_OK
            }

            // GET DATA (00 CA ...)
            commandApdu.size >= 2 &&
            commandApdu[0] == GET_DATA_HEADER[0] &&
            commandApdu[1] == GET_DATA_HEADER[1] -> {
                Log.d(TAG, "→ GET DATA")
                uidBytes + SW_OK
            }

            // Любой другой APDU — возвращаем UID как fallback для нестандартных ридеров
            else -> {
                Log.d(TAG, "→ Unknown APDU, UID fallback")
                uidBytes + SW_OK
            }
        }

        Log.d(TAG, "→ Response: ${response.toHex()}")
        return response
    }

    override fun onDeactivated(reason: Int) {
        val reasonStr = when (reason) {
            DEACTIVATION_LINK_LOSS -> "LINK_LOSS"
            DEACTIVATION_DESELECTED -> "DESELECTED"
            else -> "UNKNOWN($reason)"
        }
        Log.d(TAG, "HCE deactivated: $reasonStr")
    }

    private fun isSelectAid(apdu: ByteArray) =
        apdu.size >= 4 &&
        apdu[0] == SELECT_AID_HEADER[0] &&
        apdu[1] == SELECT_AID_HEADER[1] &&
        apdu[2] == SELECT_AID_HEADER[2]

    private fun isGetUid(apdu: ByteArray) =
        apdu.size >= 3 &&
        apdu[0] == GET_UID_COMMAND[0] &&
        apdu[1] == GET_UID_COMMAND[1] &&
        apdu[2] == GET_UID_COMMAND[2]
}

// --- Extensions ---

fun ByteArray.toHex(): String = joinToString("") { "%02X".format(it) }

fun String.hexToByteArray(): ByteArray {
    val clean = this.uppercase().replace(" ", "").replace(":", "")
    require(clean.length % 2 == 0) { "Hex string must have even length" }
    return ByteArray(clean.length / 2) { i ->
        ((Character.digit(clean[i * 2], 16) shl 4) +
         Character.digit(clean[i * 2 + 1], 16)).toByte()
    }
}
