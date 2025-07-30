package com.villamaster.ai.ui.screen.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.villamaster.ai.data.repository.ClientRepository
import com.villamaster.ai.data.repository.ReservationRepository
import com.villamaster.ai.data.repository.VillaRepository
import com.villamaster.ai.service.AIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel pour l'écran Assistant IA
 */
@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val aiService: AIService,
    private val villaRepository: VillaRepository,
    private val clientRepository: ClientRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssistantUiState())
    val uiState: StateFlow<AssistantUiState> = _uiState.asStateFlow()

    /**
     * Envoie un message à l'IA
     */
    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                // Ajouter le message de l'utilisateur
                val userMessage = ChatMessage(
                    content = message,
                    isFromUser = true,
                    timestamp = System.currentTimeMillis()
                )
                
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + userMessage,
                    isLoading = true,
                    error = null
                )

                // Obtenir la réponse de l'IA
                val response = aiService.processMessage(message)
                
                val aiMessage = ChatMessage(
                    content = response,
                    isFromUser = false,
                    timestamp = System.currentTimeMillis()
                )

                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + aiMessage,
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }

    /**
     * Joindre une image (ouvre la galerie)
     */
    fun attachImage() {
        // TODO: Implémenter l'ouverture de la galerie
        // et l'envoi de l'image à l'IA
    }

    /**
     * Efface l'erreur
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * État de l'interface utilisateur de l'assistant
 */
data class AssistantUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Représente un message de chat
 */
data class ChatMessage(
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long
)