package com.example.milkflow.ui.screen.bottle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkflow.data.repository.MilkFlowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BottleLogViewModel(private val repository: MilkFlowRepository) : ViewModel() {
    private val _time = MutableStateFlow(System.currentTimeMillis())
    val time: StateFlow<Long> = _time.asStateFlow()

    private val _amount = MutableStateFlow(0)
    val amount: StateFlow<Int> = _amount.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private var babyId: Long = 0L

    fun setBaby(id: Long) { babyId = id }

    fun updateTime(value: Long) { _time.value = value }
    fun updateAmount(value: Int) { _amount.value = value }
    fun updateNotes(value: String) { _notes.value = value }

    fun save(onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            repository.logBottleFeed(
                babyId = babyId,
                time = _time.value,
                amountMl = _amount.value,
                notes = _notes.value.takeIf { it.isNotBlank() }
            )
            onSaved()
        }
    }
}
