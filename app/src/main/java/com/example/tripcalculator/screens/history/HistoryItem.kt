package com.example.tripcalculator.screens.history

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tripcalculator.database.History
import com.example.tripcalculator.getStandardFormattedDate
import com.example.tripcalculator.ui.theme.Green
import com.example.tripcalculator.ui.theme.Red
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    history: History,
    onDeleteHistory: (History) -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    ElevatedCard(
        modifier.combinedClickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = LocalIndication.current,
        onLongClick = {
            showDialog = true
        },
        onClick = {}
    ), shape = MaterialTheme.shapes.small) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, 16.dp).animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(history.message, style = MaterialTheme.typography.titleMedium)
                Text(
                    getStandardFormattedDate(history.date),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                text = "${if (history.amount < 0) "-" else ""}₹${abs(history.amount)}",
                color = if (history.amount < 0) Red else Green,
                fontWeight = FontWeight.Bold
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteHistory(history)
                        showDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            },
            title = {
                Text("Delete History")
            },
            text = {
                Text("Are you sure you want to delete this history?")
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}