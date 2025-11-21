package com.example.milkflow.ui.screen.home

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class HomeUiState(
    val babyName: String = "",
    val lastFeedLabel: String = "No feeds yet",
    val lastSide: String? = null,
    val todayFeedCount: Int = 0,
    val todayVolume: Int = 0,
    val feeds: List<FeedLogEntity> = emptyList(),
    val settings: SettingsState = SettingsState()
)

class HomeViewModel(
    private val repository: MilkFlowRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val selectedDate = MutableStateFlow(System.currentTimeMillis())

    private val activeBabyFlow = repository.activeBaby.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    private val activeBabyId = activeBabyFlow.map { it?.id }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    private val feedsToday = activeBabyId.filterNotNull().flatMapLatest { babyId ->
        selectedDate.flatMapLatest { date ->
            repository.feedsForDay(babyId, startOfDay(date), endOfDay(date))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val lastFeed = activeBabyId.filterNotNull().flatMapLatest { babyId ->
        repository.lastFeed(babyId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val lastBreastFeed = activeBabyId.filterNotNull().flatMapLatest { babyId ->
        repository.lastBreastFeed(babyId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val feedCount = activeBabyId.filterNotNull().flatMapLatest { babyId ->
        selectedDate.flatMapLatest { date ->
            repository.feedCountForDay(babyId, startOfDay(date), endOfDay(date))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    private val totalVolume = activeBabyId.filterNotNull().flatMapLatest { babyId ->
        selectedDate.flatMapLatest { date ->
            repository.totalVolumeForDay(babyId, startOfDay(date), endOfDay(date))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val uiState = combine(
        activeBabyFlow,
        feedsToday,
        lastFeed,
        lastBreastFeed,
        feedCount,
        totalVolume,
        settingsRepository.settings
    ) { baby, feeds, last, lastBreast, count, volume, settings ->
        val lastFeedLabel = last?.let {
            val minutesAgo = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - it.startTime)
            "Last feed: ${minutesAgo}m ago"
        } ?: "No feeds yet"

        val lastSide = lastBreast?.breastSide

        HomeUiState(
            babyName = baby?.name ?: "",
            lastFeedLabel = lastFeedLabel,
            lastSide = lastSide,
            todayFeedCount = count,
            todayVolume = volume,
            feeds = feeds,
            settings = settings
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    init {
        viewModelScope.launch {
            repository.ensureDefaultBaby()
        }
    }
}
