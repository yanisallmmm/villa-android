package com.villamaster.ai.service

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.villamaster.ai.data.repository.ClientRepository
import com.villamaster.ai.data.repository.ReservationRepository
import com.villamaster.ai.data.repository.VillaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service pour l'intégration avec l'IA Gemini
 */
@Singleton
class AIService @Inject constructor(
    private val villaRepository: VillaRepository,
    private val clientRepository: ClientRepository,
    private val reservationRepository: ReservationRepository
) {
    
    // TODO: Remplacer par votre clé API Gemini
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "YOUR_GEMINI_API_KEY_HERE"
    )

    /**
     * Traite un message utilisateur et retourne une réponse de l'IA
     */
    suspend fun processMessage(message: String): String = withContext(Dispatchers.IO) {
        try {
            // Construire le contexte avec les données de l'application
            val context = buildContext()
            
            val prompt = """
                Tu es VillaMaster AI, un assistant intelligent pour la gestion de propriétés de location.
                
                Contexte de l'application:
                $context
                
                Instructions:
                - Tu peux aider avec la gestion des réservations, clients, et villas
                - Réponds en français de manière professionnelle et amicale
                - Si l'utilisateur demande des actions spécifiques (créer, modifier, supprimer), explique ce que tu ferais
                - Utilise les données du contexte pour donner des réponses précises
                
                Message de l'utilisateur: $message
                
                Réponse:
            """.trimIndent()

            val response = generativeModel.generateContent(
                content {
                    text(prompt)
                }
            )

            response.text ?: "Je n'ai pas pu traiter votre demande. Pouvez-vous reformuler ?"

        } catch (e: Exception) {
            "Désolé, je rencontre des difficultés techniques. Veuillez réessayer plus tard."
        }
    }

    /**
     * Construit le contexte de l'application pour l'IA
     */
    private suspend fun buildContext(): String {
        return try {
            val villaCount = villaRepository.getActiveVillaCount()
            val clientCount = clientRepository.getClientCount()
            val totalRevenue = reservationRepository.getTotalRevenue()
            
            """
            Données actuelles de l'application:
            - Nombre de villas actives: $villaCount
            - Nombre de clients: $clientCount
            - Revenus totaux: ${String.format("%.2f", totalRevenue)}€
            
            Tu peux aider l'utilisateur avec:
            - Consulter les statistiques
            - Gérer les réservations
            - Ajouter/modifier des clients
            - Vérifier les disponibilités
            - Analyser les performances
            """.trimIndent()
        } catch (e: Exception) {
            "Contexte de l'application temporairement indisponible."
        }
    }

    /**
     * Traite une image avec du texte
     */
    suspend fun processImageWithText(imageData: ByteArray, text: String): String = withContext(Dispatchers.IO) {
        try {
            // TODO: Implémenter le traitement d'image avec Gemini Vision
            "Traitement d'image non encore implémenté. Texte reçu: $text"
        } catch (e: Exception) {
            "Erreur lors du traitement de l'image."
        }
    }
}