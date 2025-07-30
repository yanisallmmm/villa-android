package com.villamaster.ai.ui.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.villamaster.ai.ui.component.KpiCard
import com.villamaster.ai.ui.component.ReservationCard
import com.villamaster.ai.ui.theme.AvailableGreen
import com.villamaster.ai.ui.theme.OccupiedRed
import java.text.NumberFormat
import java.util.*

/**
 * Écran Tableau de Bord - Vue d'ensemble des KPIs et activités
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Titre
        item {
            TopAppBar(
                title = { Text("📊 Tableau de Bord") }
            )
        }

        // KPIs principaux
        item {
            Text(
                text = "Indicateurs Clés",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                item {
                    KpiCard(
                        title = "Taux d'Occupation",
                        value = "${uiState.occupancyRate}%",
                        icon = Icons.Default.Home,
                        color = if (uiState.occupancyRate > 70) AvailableGreen else OccupiedRed
                    )
                }
                
                item {
                    KpiCard(
                        title = "Revenus du Mois",
                        value = NumberFormat.getCurrencyInstance(Locale.FRANCE)
                            .format(uiState.monthlyRevenue),
                        icon = Icons.Default.Euro,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    KpiCard(
                        title = "Réservations Actives",
                        value = uiState.activeReservations.toString(),
                        icon = Icons.Default.BookOnline,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                
                item {
                    KpiCard(
                        title = "Clients VIP",
                        value = uiState.vipClients.toString(),
                        icon = Icons.Default.Star,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        // Arrivées du jour
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Arrivées Aujourd'hui",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Badge {
                    Text(uiState.todayArrivals.size.toString())
                }
            }
        }

        if (uiState.todayArrivals.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = AvailableGreen
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Aucune arrivée prévue aujourd'hui")
                        }
                    }
                }
            }
        } else {
            items(uiState.todayArrivals) { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onClick = { /* Navigation vers détails */ }
                )
            }
        }

        // Départs du jour
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Départs Aujourd'hui",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Badge {
                    Text(uiState.todayDepartures.size.toString())
                }
            }
        }

        if (uiState.todayDepartures.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = AvailableGreen
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Aucun départ prévu aujourd'hui")
                        }
                    }
                }
            }
        } else {
            items(uiState.todayDepartures) { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onClick = { /* Navigation vers détails */ }
                )
            }
        }

        // Espacement final
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}