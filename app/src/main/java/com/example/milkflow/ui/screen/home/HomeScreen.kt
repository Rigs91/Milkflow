package com.example.milkflow.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milkflow.ui.components.FeedListItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onStartNursing: () -> Unit,
    onLogBottle: () -> Unit,
    onLogPump: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.babyName.ifBlank { "MilkFlow" }) },
                actions = {
                    IconButton(onClick = onHistory) {
                        Icon(imageVector = Icons.Default.History, contentDescription = "History")
                    }
                    IconButton(onClick = onSettings) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = state.lastFeedLabel, style = MaterialTheme.typography.titleMedium)
            state.lastSide?.let { Text(text = "Last side: $it", style = MaterialTheme.typography.bodyMedium) }
            Text(text = "Today: ${state.todayFeedCount} feeds • ${state.todayVolume}${state.settings.units}")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onStartNursing, modifier = Modifier.weight(1f)) {
                    Text("Start Nursing Session")
                }
                Button(onClick = onLogBottle, modifier = Modifier.weight(1f)) {
                    Text("Log Bottle Feed")
                }
            }
            Button(onClick = onLogPump, modifier = Modifier.fillMaxWidth()) {
                Text("Log Pumping Session")
            }

            Text(text = "Today", style = MaterialTheme.typography.titleMedium)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.feeds) { feed ->
                    FeedListItem(feed = feed, settings = state.settings)
                }
            }
        }
    }
}
