package com.villamaster.ai.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.villamaster.ai.data.database.dao.ClientDao
import com.villamaster.ai.data.database.dao.ReservationDao
import com.villamaster.ai.data.database.dao.VillaDao
import com.villamaster.ai.data.database.entity.Client
import com.villamaster.ai.data.database.entity.Reservation
import com.villamaster.ai.data.database.entity.Villa
import com.villamaster.ai.data.database.converter.DateConverter

/**
 * Base de données Room principale de l'application
 */
@Database(
    entities = [
        Villa::class,
        Client::class,
        Reservation::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class VillaMasterDatabase : RoomDatabase() {
    
    abstract fun villaDao(): VillaDao
    abstract fun clientDao(): ClientDao
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        private var INSTANCE: VillaMasterDatabase? = null

        fun getDatabase(context: Context): VillaMasterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VillaMasterDatabase::class.java,
                    "villamaster_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}