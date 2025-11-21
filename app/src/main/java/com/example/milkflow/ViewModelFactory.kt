package com.example.milkflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.milkflow.data.SettingsRepository
import com.example.milkflow.data.repository.MilkFlowRepository
import com.example.milkflow.ui.screen.bottle.BottleLogViewModel
import com.example.milkflow.ui.screen.history.HistoryViewModel
import com.example.milkflow.ui.screen.home.HomeViewModel
import com.example.milkflow.ui.screen.nursing.NursingSessionViewModel
import com.example.milkflow.ui.screen.pump.PumpLogViewModel
import com.example.milkflow.ui.screen.settings.SettingsViewModel

class MilkFlowViewModelFactory(
    private val repository: MilkFlowRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository, settingsRepository) as T
            modelClass.isAssignableFrom(NursingSessionViewModel::class.java) -> NursingSessionViewModel(repository) as T
            modelClass.isAssignableFrom(BottleLogViewModel::class.java) -> BottleLogViewModel(repository) as T
            modelClass.isAssignableFrom(PumpLogViewModel::class.java) -> PumpLogViewModel(repository) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(repository, settingsRepository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(settingsRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
