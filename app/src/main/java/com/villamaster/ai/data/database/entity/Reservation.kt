package com.villamaster.ai.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable
import java.util.Date

/**
 * Entité Reservation pour la base de données Room
 */
@Entity(
    tableName = "reservations",
    foreignKeys = [
        ForeignKey(
            entity = Villa::class,
            parentColumns = ["id"],
            childColumns = ["villaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Serializable
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val villaId: Long,
    val clientId: Long,
    val checkInDate: Long, // Timestamp
    val checkOutDate: Long, // Timestamp
    val numberOfGuests: Int = 1,
    val totalAmount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val status: ReservationStatus = ReservationStatus.CONFIRMED,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val remainingAmount: Double
        get() = totalAmount - paidAmount
    
    val isFullyPaid: Boolean
        get() = paidAmount >= totalAmount
    
    val numberOfNights: Int
        get() = ((checkOutDate - checkInDate) / (24 * 60 * 60 * 1000)).toInt()
}

/**
 * Statuts possibles d'une réservation
 */
@Serializable
enum class ReservationStatus {
    PENDING,    // En attente
    CONFIRMED,  // Confirmée
    CHECKED_IN, // Client arrivé
    CHECKED_OUT,// Client parti
    CANCELLED   // Annulée
}