package com.villamaster.ai.di

import android.content.Context
import androidx.room.Room
import com.villamaster.ai.data.database.VillaMasterDatabase
import com.villamaster.ai.data.database.dao.ClientDao
import com.villamaster.ai.data.database.dao.ReservationDao
import com.villamaster.ai.data.database.dao.VillaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module Hilt pour l'injection de dépendances de la base de données
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideVillaMasterDatabase(@ApplicationContext context: Context): VillaMasterDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            VillaMasterDatabase::class.java,
            "villamaster_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideVillaDao(database: VillaMasterDatabase): VillaDao {
        return database.villaDao()
    }

    @Provides
    fun provideClientDao(database: VillaMasterDatabase): ClientDao {
        return database.clientDao()
    }

    @Provides
    fun provideReservationDao(database: VillaMasterDatabase): ReservationDao {
        return database.reservationDao()
    }
}