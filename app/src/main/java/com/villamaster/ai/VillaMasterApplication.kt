package com.villamaster.ai

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.villamaster.ai.worker.BackupWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Application class pour VillaMaster AI
 * Initialise Hilt et configure les tâches de sauvegarde automatique
 */
@HiltAndroidApp
class VillaMasterApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        
        // Planifier la sauvegarde automatique quotidienne
        scheduleAutomaticBackup()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    /**
     * Planifie une sauvegarde automatique quotidienne
     */
    private fun scheduleAutomaticBackup() {
        val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(24, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "automatic_backup",
            ExistingPeriodicWorkPolicy.KEEP,
            backupRequest
        )
    }
}