package com.villamaster.ai.ui.screen.assistant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.villamaster.ai.ui.component.ChatMessage
import com.villamaster.ai.ui.component.ChatMessageType

/**
 * Écran Assistant IA - Interface de chat principal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(
    viewModel: AssistantViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var messageText by remember { mutableStateOf("") }

    // Auto-scroll vers le bas quand de nouveaux messages arrivent
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Barre de titre
        TopAppBar(
            title = { Text("🤖 Assistant VillaMaster") }
        )

        // Liste des messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.messages.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "👋 Bonjour !",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Je suis votre assistant VillaMaster AI. Je peux vous aider à :",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column {
                                Text("• Gérer vos réservations")
                                Text("• Consulter les disponibilités")
                                Text("• Ajouter des clients")
                                Text("• Analyser vos données")
                                Text("• Et bien plus encore !")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Que puis-je faire pour vous ?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            items(uiState.messages) { message ->
                ChatMessage(
                    message = message.content,
                    type = if (message.isFromUser) ChatMessageType.USER else ChatMessageType.ASSISTANT,
                    timestamp = message.timestamp
                )
            }

            if (uiState.isLoading) {
                item {
                    ChatMessage(
                        message = "Je réfléchis...",
                        type = ChatMessageType.ASSISTANT,
                        isLoading = true
                    )
                }
            }
        }

        // Zone de saisie
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Bouton joindre image
                IconButton(
                    onClick = { viewModel.attachImage() }
                ) {
                    Icon(
                        Icons.Default.AttachFile,
                        contentDescription = "Joindre une image"
                    )
                }

                // Champ de texte
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tapez votre message...") },
                    maxLines = 4,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Bouton envoyer
                FloatingActionButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Envoyer",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

    // Afficher les erreurs
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Afficher un snackbar ou un toast avec l'erreur
        }
    }
}