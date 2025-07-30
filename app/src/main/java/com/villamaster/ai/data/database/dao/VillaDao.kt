package com.villamaster.ai.data.database.dao

import androidx.room.*
import com.villamaster.ai.data.database.entity.Villa
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur les villas
 */
@Dao
interface VillaDao {
    
    @Query("SELECT * FROM villas WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveVillas(): Flow<List<Villa>>
    
    @Query("SELECT * FROM villas ORDER BY name ASC")
    fun getAllVillas(): Flow<List<Villa>>
    
    @Query("SELECT * FROM villas WHERE id = :id")
    suspend fun getVillaById(id: Long): Villa?
    
    @Query("SELECT * FROM villas WHERE name LIKE '%' || :searchQuery || '%' AND isActive = 1")
    fun searchVillas(searchQuery: String): Flow<List<Villa>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVilla(villa: Villa): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVillas(villas: List<Villa>)
    
    @Update
    suspend fun updateVilla(villa: Villa)
    
    @Delete
    suspend fun deleteVilla(villa: Villa)
    
    @Query("DELETE FROM villas")
    suspend fun deleteAllVillas()
    
    @Query("SELECT COUNT(*) FROM villas WHERE isActive = 1")
    suspend fun getActiveVillaCount(): Int
    
    @Query("""
        SELECT v.* FROM villas v 
        WHERE v.isActive = 1 
        AND v.id NOT IN (
            SELECT r.villaId FROM reservations r 
            WHERE (r.checkInDate <= :endDate AND r.checkOutDate >= :startDate)
            AND r.status IN ('CONFIRMED', 'CHECKED_IN')
        )
        ORDER BY v.name ASC
    """)
    suspend fun getAvailableVillas(startDate: Long, endDate: Long): List<Villa>
}