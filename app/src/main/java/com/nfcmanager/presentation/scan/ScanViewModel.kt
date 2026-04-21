package com.nfcmanager.presentation.scan

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nfcmanager.domain.model.NfcCard
import com.nfcmanager.domain.usecase.SaveCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ScanUiState {
    object WaitingForCard : ScanUiState()
    object Reading : ScanUiState()
    data class CardRead(val card: NfcCard) : ScanUiState()
    data class Saved(val card: NfcCard) : ScanUiState()
    data class Error(val message: String) : ScanUiState()
}

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val saveCard: SaveCardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScanUiState>(ScanUiState.WaitingForCard)
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun onTagDiscovered(tag: Tag) {
        viewModelScope.launch {
            _uiState.value = ScanUiState.Reading
            try {
                val card = NfcReader.readTag(tag)
                if (card != null) {
                    _uiState.value = ScanUiState.CardRead(card)
                } else {
                    _uiState.value = ScanUiState.Error("Не удалось прочитать карту")
                }
            } catch (e: Exception) {
                _uiState.value = ScanUiState.Error("Ошибка: ${e.message}")
            }
        }
    }

    fun onSaveCard(card: NfcCard, customName: String) {
        viewModelScope.launch {
            try {
                val cardToSave = if (customName.isNotBlank()) card.copy(name = customName) else card
                saveCard(cardToSave)
                _uiState.value = ScanUiState.Saved(cardToSave)
            } catch (e: Exception) {
                _uiState.value = ScanUiState.Error("Ошибка сохранения: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = ScanUiState.WaitingForCard
    }
}
