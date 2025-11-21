package com.example.milkflow.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkflow.data.SettingsRepository
import com.example.milkflow.data.SettingsState
import com.example.milkflow.data.endOfDay
import com.example.milkflow.data.local.FeedLogEntity
import com.example.milkflow.data.repository.MilkFlowRepository
import com.example.milkflow.data.startOfDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class HistoryUiState(
    val selectedDate: Long = System.currentTimeMillis(),
    val feeds: List<FeedLogEntity> = emptyList(),
    val totalFeeds: Int = 0,
    val totalVolume: Int = 0,
    val settings: SettingsState = SettingsState()
)

class HistoryViewModel(
    private val repository: MilkFlowRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val selectedDate = MutableStateFlow(System.currentTimeMillis())

    private val activeBabyId = repository.activeBaby.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    private val feeds = activeBabyId.filterNotNull().flatMapLatest { baby ->
        selectedDate.flatMapLatest { date ->
            repository.feedsForDay(baby.id, startOfDay(date), endOfDay(date))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val feedCount = activeBabyId.filterNotNull().flatMapLatest { baby ->
        selectedDate.flatMapLatest { date ->
            repository.feedCountForDay(baby.id, startOfDay(date), endOfDay(date))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    private val volume = activeBabyId.filterNotNull().flatMapLatest { baby ->
        selectedDate.flatMapLatest { date ->
            repository.totalVolumeForDay(baby.id, startOfDay(date), endOfDay(date))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val uiState = combine(
        selectedDate,
        feeds,
        feedCount,
        volume,
        settingsRepository.settings
    ) { date, list, count, vol, settings ->
        HistoryUiState(
            selectedDate = date,
            feeds = list,
            totalFeeds = count,
            totalVolume = vol,
            settings = settings
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState())

    fun nextDay() {
        shiftDay(1)
    }

    fun previousDay() {
        shiftDay(-1)
    }

    private fun shiftDay(delta: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = selectedDate.value
        cal.add(Calendar.DAY_OF_YEAR, delta)
        selectedDate.value = cal.timeInMillis
    }
}
