package com.villamaster.ai.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.villamaster.ai.data.database.dao.ReservationWithDetails
import com.villamaster.ai.data.database.entity.ReservationStatus
import com.villamaster.ai.data.repository.ClientRepository
import com.villamaster.ai.data.repository.ReservationRepository
import com.villamaster.ai.data.repository.VillaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel pour l'écran Dashboard
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val villaRepository: VillaRepository,
    private val clientRepository: ClientRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    /**
     * Charge toutes les données du tableau de bord
     */
    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                val today = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val endOfDay = today + (24 * 60 * 60 * 1000) - 1

                // Charger les KPIs
                val totalVillas = villaRepository.getActiveVillaCount()
                val activeReservations = reservationRepository.getReservationCountByStatus(ReservationStatus.CONFIRMED) +
                        reservationRepository.getReservationCountByStatus(ReservationStatus.CHECKED_IN)
                val monthlyRevenue = reservationRepository.getTotalRevenue()
                val vipClientCount = clientRepository.getVipClients()

                // Calculer le taux d'occupation
                val occupancyRate = if (totalVillas > 0) {
                    (activeReservations * 100) / totalVillas
                } else 0

                // Charger les arrivées et départs du jour
                val todayArrivals = reservationRepository.getArrivalsForDate(today)
                val todayDepartures = reservationRepository.getDeparturesForDate(today)

                _uiState.value = _uiState.value.copy(
                    occupancyRate = occupancyRate,
                    monthlyRevenue = monthlyRevenue,
                    activeReservations = activeReservations,
                    vipClients = 0, // Sera mis à jour avec le flow
                    todayArrivals = emptyList(), // Sera converti en ReservationWithDetails
                    todayDepartures = emptyList(), // Sera converti en ReservationWithDetails
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erreur lors du chargement des données"
                )
            }
        }
    }

    /**
     * Actualise les données du tableau de bord
     */
    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        loadDashboardData()
    }
}

/**
 * État de l'interface utilisateur du tableau de bord
 */
data class DashboardUiState(
    val occupancyRate: Int = 0,
    val monthlyRevenue: Double = 0.0,
    val activeReservations: Int = 0,
    val vipClients: Int = 0,
    val todayArrivals: List<ReservationWithDetails> = emptyList(),
    val todayDepartures: List<ReservationWithDetails> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)