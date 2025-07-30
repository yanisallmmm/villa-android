package com.villamaster.ai.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.villamaster.ai.R
import com.villamaster.ai.ui.screen.assistant.AssistantScreen
import com.villamaster.ai.ui.screen.dashboard.DashboardScreen
import com.villamaster.ai.ui.screen.reservations.ReservationsScreen
import com.villamaster.ai.ui.screen.availability.AvailabilityScreen
import com.villamaster.ai.ui.screen.clients.ClientsScreen
import com.villamaster.ai.ui.screen.villas.VillasScreen
import com.villamaster.ai.ui.screen.settings.SettingsScreen

/**
 * Navigation principale de l'application VillaMaster AI
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VillaMasterNavigation() {
    val navController = rememberNavController()
    var showMoreMenu by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Onglets principaux
                val mainTabs = listOf(
                    BottomNavItem.Assistant,
                    BottomNavItem.Dashboard,
                    BottomNavItem.Reservations,
                    BottomNavItem.Availability
                )

                mainTabs.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.title)) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                // Bouton "Plus"
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MoreHoriz, contentDescription = null) },
                    label = { Text("Plus") },
                    selected = false,
                    onClick = { showMoreMenu = true }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Assistant.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Assistant.route) {
                AssistantScreen()
            }
            composable(BottomNavItem.Dashboard.route) {
                DashboardScreen()
            }
            composable(BottomNavItem.Reservations.route) {
                ReservationsScreen()
            }
            composable(BottomNavItem.Availability.route) {
                AvailabilityScreen()
            }
            composable("clients") {
                ClientsScreen()
            }
            composable("villas") {
                VillasScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
        }

        // Menu "Plus"
        if (showMoreMenu) {
            DropdownMenu(
                expanded = showMoreMenu,
                onDismissRequest = { showMoreMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("👥 Clients") },
                    onClick = {
                        showMoreMenu = false
                        navController.navigate("clients")
                    }
                )
                DropdownMenuItem(
                    text = { Text("🏡 Villas") },
                    onClick = {
                        showMoreMenu = false
                        navController.navigate("villas")
                    }
                )
                DropdownMenuItem(
                    text = { Text("⚙️ Paramètres") },
                    onClick = {
                        showMoreMenu = false
                        navController.navigate("settings")
                    }
                )
            }
        }
    }
}

/**
 * Éléments de navigation du bas
 */
sealed class BottomNavItem(
    val route: String,
    val title: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Assistant : BottomNavItem("assistant", R.string.nav_assistant, Icons.Default.SmartToy)
    object Dashboard : BottomNavItem("dashboard", R.string.nav_dashboard, Icons.Default.Dashboard)
    object Reservations : BottomNavItem("reservations", R.string.nav_reservations, Icons.Default.BookOnline)
    object Availability : BottomNavItem("availability", R.string.nav_availability, Icons.Default.CalendarMonth)
}