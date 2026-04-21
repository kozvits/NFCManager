package com.nfcmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nfc_cards")
data class NfcCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uid: String,
    val name: String,
    val cardType: String,
    val techListJson: String,
    val sectorDataJson: String,
    val pagesJson: String,
    val createdAt: Long,
    val isSelected: Boolean = false
)
