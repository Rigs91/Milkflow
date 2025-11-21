package com.example.milkflow.ui.screen.nursing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NursingSessionScreen(
    viewModel: NursingSessionViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nursing Session") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Side")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("LEFT", "RIGHT", "BOTH").forEach { side ->
                    val selected = state.side == side
                    if (selected) {
                        Button(onClick = { viewModel.selectSide(side) }) { Text(side) }
                    } else {
                        OutlinedButton(onClick = { viewModel.selectSide(side) }) { Text(side) }
                    }
                }
            }

            val minutes = state.elapsedSeconds / 60
            val seconds = state.elapsedSeconds % 60
            Text(text = String.format("%02d:%02d", minutes, seconds))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (!state.isRunning) {
                    Button(onClick = { viewModel.startSession() }, modifier = Modifier.weight(1f)) {
                        Text("Start")
                    }
                } else {
                    Button(onClick = { viewModel.stopSession(onSaved = onBack) }, modifier = Modifier.weight(1f)) {
                        Text("Stop & Save")
                    }
                }
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
            }
        }
    }
}
