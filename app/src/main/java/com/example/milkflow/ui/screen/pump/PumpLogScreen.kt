package com.example.milkflow.ui.screen.pump

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.milkflow.data.formatTime

@Composable
fun PumpLogScreen(
    viewModel: PumpLogViewModel,
    is24Hour: Boolean,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val start by viewModel.startTime.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val side by viewModel.side.collectAsState()
    val notes by viewModel.notes.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Pump Session") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Start: ${formatTime(start, is24Hour)}")
            OutlinedTextField(
                value = duration.toString(),
                onValueChange = { viewModel.updateDuration(it.toIntOrNull() ?: 0) },
                label = { Text("Duration (min)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = amount.toString(),
                onValueChange = { viewModel.updateAmount(it.toIntOrNull() ?: 0) },
                label = { Text("Amount (ml)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("LEFT", "RIGHT", "BOTH").forEach { option ->
                    if (option == side) {
                        Button(onClick = { viewModel.updateSide(option) }) { Text(option) }
                    } else {
                        OutlinedButton(onClick = { viewModel.updateSide(option) }) { Text(option) }
                    }
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = viewModel::updateNotes,
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = { viewModel.save(onSaved) }, modifier = Modifier.fillMaxWidth()) { Text("Save") }
            Button(onClick = onCancel, modifier = Modifier.fillMaxWidth()) { Text("Cancel") }
        }
    }
}
