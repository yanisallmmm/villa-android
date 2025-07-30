package com.villamaster.ai.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.villamaster.ai.data.database.dao.ReservationWithDetails
import com.villamaster.ai.data.database.entity.ReservationStatus
import com.villamaster.ai.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Composant pour afficher une carte de réservation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationCard(
    reservation: ReservationWithDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // En-tête avec statut
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reservation.villaName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // Pastille de statut
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = when (reservation.status) {
                            ReservationStatus.CONFIRMED -> StatusConfirmed
                            ReservationStatus.PENDING -> StatusPending
                            ReservationStatus.CHECKED_IN -> StatusCheckedIn
                            ReservationStatus.CHECKED_OUT -> StatusCheckedOut
                            ReservationStatus.CANCELLED -> StatusCancelled
                        },
                        shape = CircleShape
                    ) {}
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Informations client
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = reservation.clientFullName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Dates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Arrivée",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(Date(reservation.checkInDate)),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Column {
                    Text(
                        text = "Départ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(Date(reservation.checkOutDate)),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Column {
                    Text(
                        text = "Montant",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = NumberFormat.getCurrencyInstance(Locale.FRANCE)
                            .format(reservation.totalAmount),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // Statut textuel
            if (reservation.status != ReservationStatus.CONFIRMED) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (reservation.status) {
                        ReservationStatus.PENDING -> "En attente"
                        ReservationStatus.CHECKED_IN -> "Client présent"
                        ReservationStatus.CHECKED_OUT -> "Terminée"
                        ReservationStatus.CANCELLED -> "Annulée"
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = when (reservation.status) {
                        ReservationStatus.PENDING -> StatusPending
                        ReservationStatus.CHECKED_IN -> StatusCheckedIn
                        ReservationStatus.CHECKED_OUT -> StatusCheckedOut
                        ReservationStatus.CANCELLED -> StatusCancelled
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}