package com.villamaster.ai.service

import android.content.Context
import com.villamaster.ai.data.database.VillaMasterDatabase
import com.villamaster.ai.data.database.entity.Client
import com.villamaster.ai.data.database.entity.Reservation
import com.villamaster.ai.data.database.entity.Villa
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service pour la gestion des sauvegardes et restaurations
 */
@Singleton
class BackupService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: VillaMasterDatabase
) {

    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    /**
     * Effectue une sauvegarde automatique interne (silencieuse)
     */
    suspend fun performAutomaticBackup(): Boolean = withContext(Dispatchers.IO) {
        try {
            val backupData = createBackupData()
            val backupJson = json.encodeToString(backupData)
            
            // Sauvegarder dans le dossier privé de l'application
            val internalDir = File(context.filesDir, "backups")
            if (!internalDir.exists()) {
                internalDir.mkdirs()
            }
            
            val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
                .format(Date())
            val backupFile = File(internalDir, "auto_backup_$timestamp.json")
            
            backupFile.writeText(backupJson)
            
            // Nettoyer les anciennes sauvegardes (garder seulement les 7 dernières)
            cleanOldBackups(internalDir)
            
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Exporte les données vers le dossier Téléchargements
     */
    suspend fun exportData(): String = withContext(Dispatchers.IO) {
        val backupData = createBackupData()
        val backupJson = json.encodeToString(backupData)
        
        // Créer le fichier dans le dossier Téléchargements
        val downloadsDir = File(context.getExternalFilesDir(null), "Downloads")
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }
        
        val timestamp = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val backupFile = File(downloadsDir, "VillaMasterAI_Backup_$timestamp.json")
        
        backupFile.writeText(backupJson)
        backupFile.absolutePath
    }

    /**
     * Importe les données depuis un fichier de sauvegarde
     */
    suspend fun importData(filePath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val backupFile = File(filePath)
            if (!backupFile.exists()) {
                return@withContext false
            }
            
            val backupJson = backupFile.readText()
            val backupData = json.decodeFromString<BackupData>(backupJson)
            
            // Effacer toutes les données existantes
            database.clearAllTables()
            
            // Restaurer les données
            database.villaDao().insertVillas(backupData.villas)
            database.clientDao().insertClients(backupData.clients)
            database.reservationDao().insertReservations(backupData.reservations)
            
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Réinitialise l'application avec des données de démonstration
     */
    suspend fun resetApplication(): Boolean = withContext(Dispatchers.IO) {
        try {
            // Effacer toutes les données
            database.clearAllTables()
            
            // Insérer des données de démonstration
            insertDemoData()
            
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Crée les données de sauvegarde
     */
    private suspend fun createBackupData(): BackupData {
        val villas = database.villaDao().getAllVillas()
        val clients = database.clientDao().getAllClients()
        val reservations = database.reservationDao().getAllReservations()
        
        // Convertir les Flow en listes (prendre la première émission)
        return BackupData(
            villas = emptyList(), // TODO: Récupérer les données actuelles
            clients = emptyList(), // TODO: Récupérer les données actuelles
            reservations = emptyList(), // TODO: Récupérer les données actuelles
            exportDate = System.currentTimeMillis(),
            version = 1
        )
    }

    /**
     * Nettoie les anciennes sauvegardes automatiques
     */
    private fun cleanOldBackups(backupDir: File) {
        val backupFiles = backupDir.listFiles { file ->
            file.name.startsWith("auto_backup_") && file.name.endsWith(".json")
        }?.sortedByDescending { it.lastModified() }
        
        // Garder seulement les 7 dernières sauvegardes
        backupFiles?.drop(7)?.forEach { it.delete() }
    }

    /**
     * Insère des données de démonstration
     */
    private suspend fun insertDemoData() {
        // Insérer des villas de démonstration
        val demoVillas = listOf(
            Villa(
                name = "Villa Sunset",
                description = "Magnifique villa avec vue sur mer",
                address = "123 Avenue de la Plage, Nice",
                capacity = 6,
                pricePerNight = 250.0,
                amenities = """["Piscine", "WiFi", "Climatisation", "Parking"]"""
            ),
            Villa(
                name = "Villa Paradise",
                description = "Villa luxueuse avec jardin tropical",
                address = "456 Rue des Palmiers, Cannes",
                capacity = 8,
                pricePerNight = 350.0,
                amenities = """["Piscine", "Jacuzzi", "WiFi", "Jardin", "BBQ"]"""
            )
        )
        database.villaDao().insertVillas(demoVillas)

        // Insérer des clients de démonstration
        val demoClients = listOf(
            Client(
                firstName = "Jean",
                lastName = "Dupont",
                email = "jean.dupont@email.com",
                phone = "+33 6 12 34 56 78"
            ),
            Client(
                firstName = "Marie",
                lastName = "Martin",
                email = "marie.martin@email.com",
                phone = "+33 6 87 65 43 21",
                isVip = true
            )
        )
        database.clientDao().insertClients(demoClients)
    }
}

/**
 * Structure des données de sauvegarde
 */
@Serializable
data class BackupData(
    val villas: List<Villa>,
    val clients: List<Client>,
    val reservations: List<Reservation>,
    val exportDate: Long,
    val version: Int
)