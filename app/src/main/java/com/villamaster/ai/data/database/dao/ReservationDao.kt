package com.villamaster.ai.data.database.dao

import androidx.room.*
import com.villamaster.ai.data.database.entity.Reservation
import com.villamaster.ai.data.database.entity.ReservationStatus
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur les réservations
 */
@Dao
interface ReservationDao {
    
    @Query("""
        SELECT r.*, v.name as villaName, c.firstName, c.lastName 
        FROM reservations r
        INNER JOIN villas v ON r.villaId = v.id
        INNER JOIN clients c ON r.clientId = c.id
        ORDER BY r.checkInDate DESC
    """)
    fun getAllReservationsWithDetails(): Flow<List<ReservationWithDetails>>
    
    @Query("SELECT * FROM reservations ORDER BY checkInDate DESC")
    fun getAllReservations(): Flow<List<Reservation>>
    
    @Query("SELECT * FROM reservations WHERE id = :id")
    suspend fun getReservationById(id: Long): Reservation?
    
    @Query("SELECT * FROM reservations WHERE villaId = :villaId ORDER BY checkInDate DESC")
    fun getReservationsByVilla(villaId: Long): Flow<List<Reservation>>
    
    @Query("SELECT * FROM reservations WHERE clientId = :clientId ORDER BY checkInDate DESC")
    fun getReservationsByClient(clientId: Long): Flow<List<Reservation>>
    
    @Query("SELECT * FROM reservations WHERE status = :status ORDER BY checkInDate ASC")
    fun getReservationsByStatus(status: ReservationStatus): Flow<List<Reservation>>
    
    @Query("""
        SELECT * FROM reservations 
        WHERE checkInDate >= :startDate AND checkInDate <= :endDate
        ORDER BY checkInDate ASC
    """)
    suspend fun getReservationsInDateRange(startDate: Long, endDate: Long): List<Reservation>
    
    @Query("""
        SELECT * FROM reservations 
        WHERE DATE(checkInDate/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')
        AND status IN ('CONFIRMED', 'CHECKED_IN')
        ORDER BY checkInDate ASC
    """)
    suspend fun getArrivalsForDate(date: Long): List<Reservation>
    
    @Query("""
        SELECT * FROM reservations 
        WHERE DATE(checkOutDate/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')
        AND status = 'CHECKED_IN'
        ORDER BY checkOutDate ASC
    """)
    suspend fun getDeparturesForDate(date: Long): List<Reservation>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservations(reservations: List<Reservation>)
    
    @Update
    suspend fun updateReservation(reservation: Reservation)
    
    @Delete
    suspend fun deleteReservation(reservation: Reservation)
    
    @Query("DELETE FROM reservations")
    suspend fun deleteAllReservations()
    
    @Query("SELECT COUNT(*) FROM reservations WHERE status = :status")
    suspend fun getReservationCountByStatus(status: ReservationStatus): Int
    
    @Query("SELECT SUM(totalAmount) FROM reservations WHERE status != 'CANCELLED'")
    suspend fun getTotalRevenue(): Double?
    
    @Query("SELECT SUM(paidAmount) FROM reservations WHERE status != 'CANCELLED'")
    suspend fun getTotalPaidAmount(): Double?
    
    @Query("""
        SELECT COUNT(*) FROM reservations 
        WHERE villaId = :villaId 
        AND ((checkInDate <= :endDate AND checkOutDate >= :startDate))
        AND status IN ('CONFIRMED', 'CHECKED_IN')
        AND id != :excludeReservationId
    """)
    suspend fun checkVillaAvailability(
        villaId: Long, 
        startDate: Long, 
        endDate: Long, 
        excludeReservationId: Long = -1
    ): Int
}

/**
 * Classe de données pour les réservations avec détails
 */
data class ReservationWithDetails(
    val id: Long,
    val villaId: Long,
    val clientId: Long,
    val checkInDate: Long,
    val checkOutDate: Long,
    val numberOfGuests: Int,
    val totalAmount: Double,
    val paidAmount: Double,
    val status: ReservationStatus,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long,
    val villaName: String,
    val firstName: String,
    val lastName: String
) {
    val clientFullName: String
        get() = "$firstName $lastName"
    
    val remainingAmount: Double
        get() = totalAmount - paidAmount
}