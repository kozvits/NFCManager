package com.nfcmanager.presentation.scan

import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import com.nfcmanager.domain.model.CardType
import com.nfcmanager.domain.model.NfcCard
import com.nfcmanager.presentation.emulation.toHex
import java.io.IOException

object NfcReader {

    fun readTag(tag: Tag): NfcCard? {
        val uid = tag.id.toHex()
        val techList = tag.techList.toList()

        return when {
            techList.contains(MifareClassic::class.java.name) -> readMifareClassic(tag, uid, techList)
            techList.contains(MifareUltralight::class.java.name) -> readMifareUltralight(tag, uid, techList)
            else -> readGenericNfcA(tag, uid, techList)
        }
    }

    private fun readMifareClassic(tag: Tag, uid: String, techList: List<String>): NfcCard? {
        val mfc = MifareClassic.get(tag) ?: return null
        val sectorData = mutableMapOf<Int, List<String>>()
        val cardType = when (mfc.type) {
            MifareClassic.TYPE_CLASSIC -> if (mfc.size == MifareClassic.SIZE_1K) CardType.MIFARE_CLASSIC_1K else CardType.MIFARE_CLASSIC_4K
            MifareClassic.TYPE_PLUS -> if (mfc.size <= MifareClassic.SIZE_1K) CardType.MIFARE_CLASSIC_1K else CardType.MIFARE_CLASSIC_4K
            else -> CardType.MIFARE_CLASSIC_1K
        }

        try {
            mfc.connect()
            for (sector in 0 until mfc.sectorCount) {
                val authenticated = try {
                    mfc.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT) ||
                    mfc.authenticateSectorWithKeyA(sector, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY) ||
                    mfc.authenticateSectorWithKeyB(sector, MifareClassic.KEY_DEFAULT)
                } catch (e: Exception) { false }

                if (authenticated) {
                    val blocks = mutableListOf<String>()
                    val firstBlock = mfc.sectorToBlock(sector)
                    val blockCount = mfc.getBlockCountInSector(sector)
                    for (b in firstBlock until firstBlock + blockCount) {
                        try {
                            blocks.add(mfc.readBlock(b).toHex())
                        } catch (e: IOException) {
                            blocks.add("????????????????????????????????")
                        }
                    }
                    sectorData[sector] = blocks
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try { mfc.close() } catch (_: Exception) {}
        }

        return NfcCard(
            uid = uid,
            name = "Mifare Classic (${uid.chunked(2).joinToString(":")})",
            cardType = cardType,
            techList = techList,
            sectorData = sectorData
        )
    }

    private fun readMifareUltralight(tag: Tag, uid: String, techList: List<String>): NfcCard? {
        val mul = MifareUltralight.get(tag) ?: return null
        val pages = mutableListOf<String>()

        try {
            mul.connect()
            val pageCount = if (mul.type == MifareUltralight.TYPE_ULTRALIGHT_C) 48 else 16
            for (page in 0 until pageCount step 4) {
                try {
                    val data = mul.readPages(page)
                    for (i in 0 until 4) {
                        if (page + i < pageCount) {
                            pages.add(data.copyOfRange(i * 4, i * 4 + 4).toHex())
                        }
                    }
                } catch (e: IOException) {
                    repeat(4) { pages.add("????????") }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try { mul.close() } catch (_: Exception) {}
        }

        return NfcCard(
            uid = uid,
            name = "Mifare Ultralight (${uid.chunked(2).joinToString(":")})",
            cardType = CardType.MIFARE_ULTRALIGHT,
            techList = techList,
            pages = pages
        )
    }

    private fun readGenericNfcA(tag: Tag, uid: String, techList: List<String>): NfcCard {
        return NfcCard(
            uid = uid,
            name = "NFC Card (${uid.chunked(2).joinToString(":")})",
            cardType = CardType.UNKNOWN,
            techList = techList
        )
    }
}
