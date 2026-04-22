package com.nfcmanager.domain.model

data class NfcCard(
    val id: Long = 0,
    val uid: String,        // MSB (как карта передаёт по RF): BA85AC95
    val uidLsb: String = uid.chunked(2).reversed().joinToString(""), // LSB (как показывает СКУД): 95AC85BA
    val name: String,
    val cardType: CardType,
    val techList: List<String>,
    val sectorData: Map<Int, List<String>> = emptyMap(),
    val pages: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val isSelected: Boolean = false
) {
    /**
     * UID в формате для отображения с разделителями
     * MSB (порядок Android/RF): BA:85:AC:95
     */
    val uidFormatted: String get() = uid.chunked(2).joinToString(":")

    /**
     * UID в формате как показывает большинство СКУД считывателей
     * LSB (обратный порядок): 95:AC:85:BA
     * Некоторые системы добавляют ведущий 00: 00:95:AC:85:BA
     */
    val uidLsbFormatted: String get() = uidLsb.chunked(2).joinToString(":")

    /**
     * Десятичное представление UID (часто используется в СКУД)
     */
    val uidDecimal: Long get() = try {
        uid.toLong(16)
    } catch (e: Exception) { 0L }
}

enum class CardType {
    MIFARE_CLASSIC_1K,
    MIFARE_CLASSIC_4K,
    MIFARE_ULTRALIGHT,
    UNKNOWN
}
