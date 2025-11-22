package com.example.milkflow.ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milkflow.ui.components.FeedListItem
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HistoryViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Scaffold(topBar = { TopAppBar(title = { Text("History") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { viewModel.previousDay() }) { Text("Previous") }
                Text(text = formatter.format(state.selectedDate))
                Button(onClick = { viewModel.nextDay() }) { Text("Next") }
            }
            Text(text = "Total feeds: ${state.totalFeeds}")
            Text(text = "Total volume: ${state.totalVolume}${state.settings.units}")

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.feeds) { feed ->
                    FeedListItem(feed = feed, settings = state.settings)
                }
            }

            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
        }
    }
}
