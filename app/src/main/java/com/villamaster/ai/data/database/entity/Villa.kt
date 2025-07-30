package com.villamaster.ai.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Entité Villa pour la base de données Room
 */
@Entity(tableName = "villas")
@Serializable
data class Villa(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val address: String = "",
    val capacity: Int = 1,
    val pricePerNight: Double = 0.0,
    val amenities: String = "", // JSON string des équipements
    val images: String = "", // JSON string des URLs d'images
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)