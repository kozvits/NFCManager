package com.nfcmanager.data.db

import androidx.room.*
import com.nfcmanager.data.model.NfcCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NfcCardDao {
    @Query("SELECT * FROM nfc_cards ORDER BY createdAt DESC")
    fun getAllCards(): Flow<List<NfcCardEntity>>

    @Query("SELECT * FROM nfc_cards WHERE id = :id")
    suspend fun getCardById(id: Long): NfcCardEntity?

    @Query("SELECT * FROM nfc_cards WHERE isSelected = 1 LIMIT 1")
    suspend fun getSelectedCard(): NfcCardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: NfcCardEntity): Long

    @Query("DELETE FROM nfc_cards WHERE id = :id")
    suspend fun deleteCard(id: Long)

    @Query("UPDATE nfc_cards SET isSelected = 0")
    suspend fun clearAllSelections()

    @Query("UPDATE nfc_cards SET isSelected = 1 WHERE id = :id")
    suspend fun setSelectedCard(id: Long)
}
