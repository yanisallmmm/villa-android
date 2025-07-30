package com.villamaster.ai

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.villamaster.ai.ui.navigation.VillaMasterNavigation
import com.villamaster.ai.ui.theme.VillaMasterAITheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activité principale de l'application VillaMaster AI
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Launcher pour demander les permissions
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Traiter les résultats des permissions
        permissions.entries.forEach { entry ->
            val permission = entry.key
            val isGranted = entry.value
            // Log ou traitement selon les besoins
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Demander les permissions nécessaires
        requestNecessaryPermissions()
        
        setContent {
            VillaMasterAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VillaMasterNavigation()
                }
            }
        }
    }

    /**
     * Demande les permissions nécessaires pour l'application
     */
    private fun requestNecessaryPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Permission de stockage selon la version Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        // Demander les permissions si nécessaire
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
}