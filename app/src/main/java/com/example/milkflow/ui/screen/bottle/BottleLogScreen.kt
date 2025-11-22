package com.example.milkflow.ui.screen.bottle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
fun BottleLogScreen(
    viewModel: BottleLogViewModel,
    is24Hour: Boolean,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val time by viewModel.time.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val notes by viewModel.notes.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Bottle Feed") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Time: ${formatTime(time, is24Hour)}")
            OutlinedTextField(
                value = amount.toString(),
                onValueChange = { value -> viewModel.updateAmount(value.toIntOrNull() ?: 0) },
                label = { Text("Amount (ml)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = notes,
                onValueChange = viewModel::updateNotes,
                label = { Text("Milk type / Notes") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = { viewModel.save(onSaved) }, modifier = Modifier.fillMaxWidth()) {
                Text("Save")
            }
            Button(onClick = onCancel, modifier = Modifier.fillMaxWidth()) { Text("Cancel") }
        }
    }
}
