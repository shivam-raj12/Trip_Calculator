package com.example.tripcalculator.screens.history

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tripcalculator.database.HistoryDatabase
import com.example.tripcalculator.ui.theme.Green
import com.example.tripcalculator.ui.theme.Red
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val database = remember { HistoryDatabase.getDatabase(context).dao }
    val historyList = database.getAll().collectAsStateWithLifecycle(emptyList()).value
    var totalAmount = remember { Animatable(0f) }

    val totalProfitAmount = historyList.filter { it.amount > 0 }.sumOf { it.amount }
    val totalLossAmount = historyList.filter { it.amount < 0 }.sumOf { abs(it.amount) }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(historyList) {
        totalAmount.animateTo(
            historyList.sumOf { it.amount }.toFloat(),
            animationSpec = tween(500)
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = MaterialTheme.shapes.small
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(horizontal = 12.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Total Balance", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "₹${totalAmount.value.toLong()}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (totalAmount.value < 0) Red else Green
                        )
                    }
                    Column(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .animateContentSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        val profit = totalProfitAmount
                        val loss = totalLossAmount
                        val max = profit.toString().length - loss.toString().length
                        Text(
                            text = if (profit > loss) {
                                "Profit:${if (max.sign == -1) " ".repeat(abs(max)) else ""} ₹${profit}"
                            } else {
                                "Loss:${if (max.sign == 1) " ".repeat(max) else ""}   ₹${loss}"
                            },
                            color = if (profit>loss) Green else Red,
                            style = MaterialTheme.typography.titleSmall,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = if (profit > loss) {
                                "Loss:${if (max.sign == 1) " ".repeat(max) else ""}   ₹${loss}"
                            } else {
                                "Profit:${if (max.sign == -1) " ".repeat(abs(max)) else ""} ₹${profit}"
                            },
                            color = if (profit>loss) Red else Green,
                            style = MaterialTheme.typography.titleSmall,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        items(
            items = historyList,
            key = { it.id }
        ) {
            HistoryItem(
                modifier = Modifier.fillMaxWidth(),
                history = it,
                onDeleteHistory = { history ->
                    coroutineScope.launch {
                        database.delete(history)
                    }
                }
            )
        }
    }
}