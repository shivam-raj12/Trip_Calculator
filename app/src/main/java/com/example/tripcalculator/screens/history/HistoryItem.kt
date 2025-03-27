package com.example.tripcalculator.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tripcalculator.database.History
import com.example.tripcalculator.getStandardFormattedDate
import com.example.tripcalculator.ui.theme.Green
import com.example.tripcalculator.ui.theme.Red
import kotlin.math.abs

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    history: History
) {
    ElevatedCard(modifier, shape = MaterialTheme.shapes.small) {
        Row(
            modifier= Modifier.padding(horizontal = 12.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(history.message, style = MaterialTheme.typography.titleMedium)
                Text(getStandardFormattedDate(history.date), style = MaterialTheme.typography.labelSmall)
            }
            Text(
                text = "${if (history.amount<0) "-" else ""}₹${abs(history.amount)}",
                color = if (history.amount < 0) Red else Green,
                fontWeight = FontWeight.Bold
            )
        }
    }
}