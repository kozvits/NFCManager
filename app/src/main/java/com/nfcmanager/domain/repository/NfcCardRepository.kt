package com.nfcmanager.domain.repository

import com.nfcmanager.domain.model.NfcCard
import kotlinx.coroutines.flow.Flow

interface NfcCardRepository {
    fun getAllCards(): Flow<List<NfcCard>>
    suspend fun getCardById(id: Long): NfcCard?
    suspend fun getSelectedCard(): NfcCard?
    suspend fun saveCard(card: NfcCard): Long
    suspend fun deleteCard(id: Long)
    suspend fun setSelectedCard(id: Long)
    suspend fun clearSelectedCard()
}
