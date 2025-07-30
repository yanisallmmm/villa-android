package com.villamaster.ai.data.repository

import com.villamaster.ai.data.database.dao.VillaDao
import com.villamaster.ai.data.database.entity.Villa
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository pour la gestion des villas
 */
@Singleton
class VillaRepository @Inject constructor(
    private val villaDao: VillaDao
) {
    
    fun getAllActiveVillas(): Flow<List<Villa>> = villaDao.getAllActiveVillas()
    
    fun getAllVillas(): Flow<List<Villa>> = villaDao.getAllVillas()
    
    suspend fun getVillaById(id: Long): Villa? = villaDao.getVillaById(id)
    
    fun searchVillas(query: String): Flow<List<Villa>> = villaDao.searchVillas(query)
    
    suspend fun insertVilla(villa: Villa): Long = villaDao.insertVilla(villa)
    
    suspend fun updateVilla(villa: Villa) = villaDao.updateVilla(villa)
    
    suspend fun deleteVilla(villa: Villa) = villaDao.deleteVilla(villa)
    
    suspend fun getActiveVillaCount(): Int = villaDao.getActiveVillaCount()
    
    suspend fun getAvailableVillas(startDate: Long, endDate: Long): List<Villa> = 
        villaDao.getAvailableVillas(startDate, endDate)
    
    suspend fun insertDemoData() {
        val demoVillas = listOf(
            Villa(
                name = "Villa Sunset",
                description = "Magnifique villa avec vue sur mer",
                address = "123 Avenue de la Plage, Nice",
                capacity = 6,
                pricePerNight = 250.0,
                amenities = """["Piscine", "WiFi", "Climatisation", "Parking"]""",
                images = """["https://example.com/villa1.jpg"]"""
            ),
            Villa(
                name = "Villa Paradise",
                description = "Villa luxueuse avec jardin tropical",
                address = "456 Rue des Palmiers, Cannes",
                capacity = 8,
                pricePerNight = 350.0,
                amenities = """["Piscine", "Jacuzzi", "WiFi", "Jardin", "BBQ"]""",
                images = """["https://example.com/villa2.jpg"]"""
            ),
            Villa(
                name = "Villa Serenity",
                description = "Villa calme et moderne",
                address = "789 Chemin de la Paix, Antibes",
                capacity = 4,
                pricePerNight = 180.0,
                amenities = """["WiFi", "Climatisation", "Terrasse"]""",
                images = """["https://example.com/villa3.jpg"]"""
            )
        )
        
        villaDao.insertVillas(demoVillas)
    }
}