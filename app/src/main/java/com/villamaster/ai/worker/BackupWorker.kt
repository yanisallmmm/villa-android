package com.villamaster.ai.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.villamaster.ai.service.BackupService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker pour effectuer les sauvegardes automatiques
 */
@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val backupService: BackupService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Effectuer la sauvegarde automatique interne
            backupService.performAutomaticBackup()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}