package com.example.milkflow.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milkflow.data.SettingsState
import com.example.milkflow.data.formatDuration
import com.example.milkflow.data.formatTime
import com.example.milkflow.data.local.FeedLogEntity

@Composable
fun FeedListItem(feed: FeedLogEntity, settings: SettingsState, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(feed.type, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = feed.breastSide?.let { "  •  $it" } ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(formatTime(feed.startTime, settings.is24Hour), style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Duration: ${formatDuration(feed.durationMinutes)}" +
                        (feed.amountMl?.let { "  •  ${it}${settings.units}" } ?: ""),
                style = MaterialTheme.typography.bodySmall
            )
            feed.notes?.takeIf { it.isNotBlank() }?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
