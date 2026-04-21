package com.nfcmanager.presentation.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nfcmanager.domain.model.NfcCard
import com.nfcmanager.domain.usecase.*
import com.nfcmanager.presentation.emulation.NFCHCEService
import com.nfcmanager.presentation.scan.hexToBytes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardsUiState(
    val cards: List<NfcCard> = emptyList(),
    val selectedCard: NfcCard? = null,
    val isEmulating: Boolean = false,
    val snackbarMessage: String? = null
)

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val getAllCards: GetAllCardsUseCase,
    private val deleteCard: DeleteCardUseCase,
    private val setSelectedCard: SetSelectedCardUseCase,
    private val getSelectedCard: GetSelectedCardUseCase,
    private val clearSelectedCard: ClearSelectedCardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    init {
        loadCards()
        loadSelectedCard()
    }

    private fun loadCards() {
        viewModelScope.launch {
            getAllCards().collect { cards ->
                _uiState.update { it.copy(cards = cards) }
            }
        }
    }

    private fun loadSelectedCard() {
        viewModelScope.launch {
            val selected = getSelectedCard()
            _uiState.update { it.copy(selectedCard = selected) }
            if (selected != null) {
                activateEmulation(selected)
            }
        }
    }

    fun onDeleteCard(id: Long) {
        viewModelScope.launch {
            val selectedCard = _uiState.value.selectedCard
            if (selectedCard?.id == id) {
                stopEmulation()
            }
            deleteCard(id)
            showSnackbar("Карта удалена")
        }
    }

    fun onSelectCard(card: NfcCard) {
        viewModelScope.launch {
            setSelectedCard(card.id)
            _uiState.update { it.copy(selectedCard = card) }
            activateEmulation(card)
            showSnackbar("Карта выбрана для эмуляции")
        }
    }

    fun onStopEmulation() {
        viewModelScope.launch {
            stopEmulation()
            clearSelectedCard()
            _uiState.update { it.copy(selectedCard = null) }
            showSnackbar("Эмуляция остановлена")
        }
    }

    private fun activateEmulation(card: NfcCard) {
        try {
            NFCHCEService.emulatedUidBytes = card.uid.hexToBytes()
            NFCHCEService.isEmulating = true
            _uiState.update { it.copy(isEmulating = true) }
        } catch (e: Exception) {
            showSnackbar("Ошибка активации эмуляции")
        }
    }

    private fun stopEmulation() {
        NFCHCEService.emulatedUidBytes = null
        NFCHCEService.isEmulating = false
        _uiState.update { it.copy(isEmulating = false) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    private fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }
}
