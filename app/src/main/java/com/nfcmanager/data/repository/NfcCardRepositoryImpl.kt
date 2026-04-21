package com.nfcmanager.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nfcmanager.data.db.NfcCardDao
import com.nfcmanager.data.model.NfcCardEntity
import com.nfcmanager.domain.model.CardType
import com.nfcmanager.domain.model.NfcCard
import com.nfcmanager.domain.repository.NfcCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NfcCardRepositoryImpl @Inject constructor(
    private val dao: NfcCardDao,
    private val gson: Gson
) : NfcCardRepository {

    override fun getAllCards(): Flow<List<NfcCard>> =
        dao.getAllCards().map { list -> list.map { it.toDomain() } }

    override suspend fun getCardById(id: Long): NfcCard? =
        dao.getCardById(id)?.toDomain()

    override suspend fun getSelectedCard(): NfcCard? =
        dao.getSelectedCard()?.toDomain()

    override suspend fun saveCard(card: NfcCard): Long =
        dao.insertCard(card.toEntity())

    override suspend fun deleteCard(id: Long) = dao.deleteCard(id)

    override suspend fun setSelectedCard(id: Long) {
        dao.clearAllSelections()
        dao.setSelectedCard(id)
    }

    override suspend fun clearSelectedCard() = dao.clearAllSelections()

    private fun NfcCardEntity.toDomain(): NfcCard {
        val techListType = object : TypeToken<List<String>>() {}.type
        val sectorDataType = object : TypeToken<Map<Int, List<String>>>() {}.type
        val pagesType = object : TypeToken<List<String>>() {}.type
        return NfcCard(
            id = id,
            uid = uid,
            name = name,
            cardType = CardType.valueOf(cardType),
            techList = gson.fromJson(techListJson, techListType),
            sectorData = gson.fromJson(sectorDataJson, sectorDataType) ?: emptyMap(),
            pages = gson.fromJson(pagesJson, pagesType) ?: emptyList(),
            createdAt = createdAt,
            isSelected = isSelected
        )
    }

    private fun NfcCard.toEntity(): NfcCardEntity = NfcCardEntity(
        id = id,
        uid = uid,
        name = name,
        cardType = cardType.name,
        techListJson = gson.toJson(techList),
        sectorDataJson = gson.toJson(sectorData),
        pagesJson = gson.toJson(pages),
        createdAt = createdAt,
        isSelected = isSelected
    )
}
