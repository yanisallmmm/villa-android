package com.villamaster.ai.data.repository

import com.villamaster.ai.data.database.dao.ClientDao
import com.villamaster.ai.data.database.entity.Client
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository pour la gestion des clients
 */
@Singleton
class ClientRepository @Inject constructor(
    private val clientDao: ClientDao
) {
    
    fun getAllClients(): Flow<List<Client>> = clientDao.getAllClients()
    
    suspend fun getClientById(id: Long): Client? = clientDao.getClientById(id)
    
    fun searchClients(query: String): Flow<List<Client>> = clientDao.searchClients(query)
    
    suspend fun getClientByEmail(email: String): Client? = clientDao.getClientByEmail(email)
    
    suspend fun getClientByPhone(phone: String): Client? = clientDao.getClientByPhone(phone)
    
    suspend fun insertClient(client: Client): Long = clientDao.insertClient(client)
    
    suspend fun updateClient(client: Client) = clientDao.updateClient(client)
    
    suspend fun deleteClient(client: Client) = clientDao.deleteClient(client)
    
    suspend fun getClientCount(): Int = clientDao.getClientCount()
    
    fun getVipClients(): Flow<List<Client>> = clientDao.getVipClients()
    
    suspend fun insertDemoData() {
        val demoClients = listOf(
            Client(
                firstName = "Jean",
                lastName = "Dupont",
                email = "jean.dupont@email.com",
                phone = "+33 6 12 34 56 78",
                address = "123 Rue de la République, Paris",
                nationality = "Française"
            ),
            Client(
                firstName = "Marie",
                lastName = "Martin",
                email = "marie.martin@email.com",
                phone = "+33 6 87 65 43 21",
                address = "456 Avenue des Champs, Lyon",
                nationality = "Française",
                isVip = true
            ),
            Client(
                firstName = "John",
                lastName = "Smith",
                email = "john.smith@email.com",
                phone = "+44 7123 456789",
                address = "789 Oxford Street, London",
                nationality = "Britannique"
            )
        )
        
        clientDao.insertClients(demoClients)
    }
}