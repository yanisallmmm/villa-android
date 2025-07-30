package com.villamaster.ai.data.database.dao

import androidx.room.*
import com.villamaster.ai.data.database.entity.Client
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur les clients
 */
@Dao
interface ClientDao {
    
    @Query("SELECT * FROM clients ORDER BY lastName ASC, firstName ASC")
    fun getAllClients(): Flow<List<Client>>
    
    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClientById(id: Long): Client?
    
    @Query("""
        SELECT * FROM clients 
        WHERE firstName LIKE '%' || :searchQuery || '%' 
        OR lastName LIKE '%' || :searchQuery || '%'
        OR email LIKE '%' || :searchQuery || '%'
        OR phone LIKE '%' || :searchQuery || '%'
        ORDER BY lastName ASC, firstName ASC
    """)
    fun searchClients(searchQuery: String): Flow<List<Client>>
    
    @Query("SELECT * FROM clients WHERE email = :email LIMIT 1")
    suspend fun getClientByEmail(email: String): Client?
    
    @Query("SELECT * FROM clients WHERE phone = :phone LIMIT 1")
    suspend fun getClientByPhone(phone: String): Client?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClients(clients: List<Client>)
    
    @Update
    suspend fun updateClient(client: Client)
    
    @Delete
    suspend fun deleteClient(client: Client)
    
    @Query("DELETE FROM clients")
    suspend fun deleteAllClients()
    
    @Query("SELECT COUNT(*) FROM clients")
    suspend fun getClientCount(): Int
    
    @Query("SELECT * FROM clients WHERE isVip = 1 ORDER BY lastName ASC, firstName ASC")
    fun getVipClients(): Flow<List<Client>>
}