package com.example.milkflow.ui.screen.pump

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkflow.data.repository.MilkFlowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PumpLogViewModel(private val repository: MilkFlowRepository) : ViewModel() {
    private val _startTime = MutableStateFlow(System.currentTimeMillis())
    val startTime: StateFlow<Long> = _startTime.asStateFlow()

    private val _duration = MutableStateFlow(10)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    private val _amount = MutableStateFlow(0)
    val amount: StateFlow<Int> = _amount.asStateFlow()

    private val _side = MutableStateFlow("BOTH")
    val side: StateFlow<String> = _side.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private var babyId: Long = 0L

    fun setBaby(id: Long) { babyId = id }

    fun updateStartTime(value: Long) { _startTime.value = value }
    fun updateDuration(value: Int) { _duration.value = value }
    fun updateAmount(value: Int) { _amount.value = value }
    fun updateSide(value: String) { _side.value = value }
    fun updateNotes(value: String) { _notes.value = value }

    fun save(onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            repository.logPump(
                babyId = babyId,
                startTime = _startTime.value,
                durationMinutes = _duration.value,
                amountMl = _amount.value,
                breastSide = _side.value,
                notes = _notes.value.takeIf { it.isNotBlank() }
            )
            onSaved()
        }
    }
}
