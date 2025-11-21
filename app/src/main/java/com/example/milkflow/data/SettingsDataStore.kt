package com.example.milkflow.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore(name = "milkflow_settings")

object SettingsKeys {
    val UNITS = stringPreferencesKey("units")
    val TIME_FORMAT_24H = booleanPreferencesKey("time_format_24h")
}

data class SettingsState(
    val units: String = "ml",
    val is24Hour: Boolean = true
)

class SettingsRepository(private val context: Context) {
    val settings: Flow<SettingsState> = context.settingsDataStore.data.map { prefs ->
        SettingsState(
            units = prefs[SettingsKeys.UNITS] ?: "ml",
            is24Hour = prefs[SettingsKeys.TIME_FORMAT_24H] ?: true
        )
    }

    suspend fun updateUnits(units: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsKeys.UNITS] = units
        }
    }

    suspend fun updateTimeFormat(is24Hour: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsKeys.TIME_FORMAT_24H] = is24Hour
        }
    }
}
