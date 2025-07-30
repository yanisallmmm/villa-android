package com.villamaster.ai.data.repository

import com.villamaster.ai.data.database.dao.ReservationDao
import com.villamaster.ai.data.database.dao.ReservationWithDetails
import com.villamaster.ai.data.database.entity.Reservation
import com.villamaster.ai.data.database.entity.ReservationStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository pour la gestion des réservations
 */
@Singleton
class ReservationRepository @Inject constructor(
    private val reservationDao: ReservationDao
) {
    
    fun getAllReservationsWithDetails(): Flow<List<ReservationWithDetails>> = 
        reservationDao.getAllReservationsWithDetails()
    
    fun getAllReservations(): Flow<List<Reservation>> = reservationDao.getAllReservations()
    
    suspend fun getReservationById(id: Long): Reservation? = reservationDao.getReservationById(id)
    
    fun getReservationsByVilla(villaId: Long): Flow<List<Reservation>> = 
        reservationDao.getReservationsByVilla(villaId)
    
    fun getReservationsByClient(clientId: Long): Flow<List<Reservation>> = 
        reservationDao.getReservationsByClient(clientId)
    
    fun getReservationsByStatus(status: ReservationStatus): Flow<List<Reservation>> = 
        reservationDao.getReservationsByStatus(status)
    
    suspend fun getReservationsInDateRange(startDate: Long, endDate: Long): List<Reservation> = 
        reservationDao.getReservationsInDateRange(startDate, endDate)
    
    suspend fun getArrivalsForDate(date: Long): List<Reservation> = 
        reservationDao.getArrivalsForDate(date)
    
    suspend fun getDeparturesForDate(date: Long): List<Reservation> = 
        reservationDao.getDeparturesForDate(date)
    
    suspend fun insertReservation(reservation: Reservation): Long = 
        reservationDao.insertReservation(reservation)
    
    suspend fun updateReservation(reservation: Reservation) = 
        reservationDao.updateReservation(reservation)
    
    suspend fun deleteReservation(reservation: Reservation) = 
        reservationDao.deleteReservation(reservation)
    
    suspend fun getReservationCountByStatus(status: ReservationStatus): Int = 
        reservationDao.getReservationCountByStatus(status)
    
    suspend fun getTotalRevenue(): Double = reservationDao.getTotalRevenue() ?: 0.0
    
    suspend fun getTotalPaidAmount(): Double = reservationDao.getTotalPaidAmount() ?: 0.0
    
    suspend fun checkVillaAvailability(
        villaId: Long, 
        startDate: Long, 
        endDate: Long, 
        excludeReservationId: Long = -1
    ): Boolean {
        return reservationDao.checkVillaAvailability(villaId, startDate, endDate, excludeReservationId) == 0
    }
    
    suspend fun insertDemoData() {
        val currentTime = System.currentTimeMillis()
        val oneDayMs = 24 * 60 * 60 * 1000L
        
        val demoReservations = listOf(
            Reservation(
                villaId = 1,
                clientId = 1,
                checkInDate = currentTime + oneDayMs,
                checkOutDate = currentTime + (5 * oneDayMs),
                numberOfGuests = 4,
                totalAmount = 1000.0,
                paidAmount = 500.0,
                status = ReservationStatus.CONFIRMED,
                notes = "Arrivée prévue en soirée"
            ),
            Reservation(
                villaId = 2,
                clientId = 2,
                checkInDate = currentTime + (7 * oneDayMs),
                checkOutDate = currentTime + (14 * oneDayMs),
                numberOfGuests = 6,
                totalAmount = 2450.0,
                paidAmount = 2450.0,
                status = ReservationStatus.CONFIRMED,
                notes = "Client VIP - Champagne de bienvenue"
            ),
            Reservation(
                villaId = 3,
                clientId = 3,
                checkInDate = currentTime - (2 * oneDayMs),
                checkOutDate = currentTime + (3 * oneDayMs),
                numberOfGuests = 2,
                totalAmount = 900.0,
                paidAmount = 900.0,
                status = ReservationStatus.CHECKED_IN,
                notes = "Séjour en cours"
            )
        )
        
        reservationDao.insertReservations(demoReservations)
    }
}