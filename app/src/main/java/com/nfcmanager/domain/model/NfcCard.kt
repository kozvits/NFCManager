package com.nfcmanager.domain.model

data class NfcCard(
    val id: Long = 0,
    val uid: String,
    val name: String,
    val cardType: CardType,
    val techList: List<String>,
    val sectorData: Map<Int, List<String>> = emptyMap(), // sector -> list of block hex strings
    val pages: List<String> = emptyList(),               // for Ultralight pages
    val createdAt: Long = System.currentTimeMillis(),
    val isSelected: Boolean = false
)

enum class CardType {
    MIFARE_CLASSIC_1K,
    MIFARE_CLASSIC_4K,
    MIFARE_ULTRALIGHT,
    UNKNOWN
}
