package com.nfcmanager.domain.usecase

import com.nfcmanager.domain.model.NfcCard
import com.nfcmanager.domain.repository.NfcCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCardsUseCase @Inject constructor(
    private val repository: NfcCardRepository
) {
    operator fun invoke(): Flow<List<NfcCard>> = repository.getAllCards()
}

class SaveCardUseCase @Inject constructor(
    private val repository: NfcCardRepository
) {
    suspend operator fun invoke(card: NfcCard): Long = repository.saveCard(card)
}

class DeleteCardUseCase @Inject constructor(
    private val repository: NfcCardRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteCard(id)
}

class SetSelectedCardUseCase @Inject constructor(
    private val repository: NfcCardRepository
) {
    suspend operator fun invoke(id: Long) = repository.setSelectedCard(id)
}

class GetSelectedCardUseCase @Inject constructor(
    private val repository: NfcCardRepository
) {
    suspend operator fun invoke(): NfcCard? = repository.getSelectedCard()
}

class ClearSelectedCardUseCase @Inject constructor(
    private val repository: NfcCardRepository
) {
    suspend operator fun invoke() = repository.clearSelectedCard()
}
