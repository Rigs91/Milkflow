package com.example.milkflow.ui.screen.nursing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkflow.data.repository.MilkFlowRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class NursingSessionState(
    val side: String = "LEFT",
    val isRunning: Boolean = false,
    val elapsedSeconds: Long = 0L
)

class NursingSessionViewModel(
    private val repository: MilkFlowRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NursingSessionState())
    val state: StateFlow<NursingSessionState> = _state.asStateFlow()

    private var startTime: Long? = null
    private var timerJob: Job? = null
    private var babyId: Long = 0L

    fun setBaby(id: Long) {
        babyId = id
    }

    fun selectSide(side: String) {
        _state.value = _state.value.copy(side = side)
    }

    fun startSession() {
        if (_state.value.isRunning) return
        startTime = System.currentTimeMillis()
        _state.value = _state.value.copy(isRunning = true, elapsedSeconds = 0)
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.isRunning) {
                delay(1000)
                val elapsed = startTime?.let { TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - it) } ?: 0
                _state.value = _state.value.copy(elapsedSeconds = elapsed)
            }
        }
    }

    fun stopSession(notes: String? = null, onSaved: () -> Unit = {}) {
        val started = startTime ?: return
        if (!_state.value.isRunning) return
        val end = System.currentTimeMillis()
        timerJob?.cancel()
        _state.value = _state.value.copy(isRunning = false)
        viewModelScope.launch {
            repository.logBreastFeed(
                babyId = babyId,
                breastSide = _state.value.side,
                startTime = started,
                endTime = end,
                durationMinutes = TimeUnit.MILLISECONDS.toMinutes(end - started).toInt(),
                notes = notes
            )
            onSaved()
        }
    }
}
