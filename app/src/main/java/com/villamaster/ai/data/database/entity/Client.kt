package com.villamaster.ai.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Entité Client pour la base de données Room
 */
@Entity(tableName = "clients")
@Serializable
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val nationality: String = "",
    val dateOfBirth: Long? = null,
    val passportNumber: String = "",
    val notes: String = "",
    val isVip: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val fullName: String
        get() = "$firstName $lastName"
}