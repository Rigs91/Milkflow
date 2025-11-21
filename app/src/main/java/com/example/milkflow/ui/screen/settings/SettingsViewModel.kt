package com.example.milkflow.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkflow.data.SettingsRepository
import com.example.milkflow.data.SettingsState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    val settings = settingsRepository.settings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SettingsState()
    )

    fun updateUnits(units: String) {
        viewModelScope.launch { settingsRepository.updateUnits(units) }
    }

    fun updateTimeFormat(is24Hour: Boolean) {
        viewModelScope.launch { settingsRepository.updateTimeFormat(is24Hour) }
    }
}
