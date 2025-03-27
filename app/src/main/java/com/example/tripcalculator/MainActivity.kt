package com.example.tripcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tripcalculator.database.History
import com.example.tripcalculator.database.HistoryDatabase
import com.example.tripcalculator.ui.theme.Green
import com.example.tripcalculator.ui.theme.Red
import com.example.tripcalculator.ui.theme.TripCalculatorTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripCalculatorTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    HomeScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val database = remember { HistoryDatabase.getDatabase(context).dao }
    var totalMoney by remember {
        mutableIntStateOf(0)
    }
    var showHistory by remember {
        mutableStateOf(false)
    }
    val history = database.getAll().collectAsStateWithLifecycle(initialValue = emptyList())
    LaunchedEffect(Unit) {
        database.getAll().collect {
            totalMoney = it.sumOf { it.amount }
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.labelMedium.toSpanStyle()) {
                    append("Total: ")
                }
                withStyle(style = MaterialTheme.typography.headlineSmall.toSpanStyle()) {
                    append("₹${abs(totalMoney)}")
                }
            }, color = if (totalMoney < 0) Red else Green, textAlign = TextAlign.Center)
        }
        LazyColumn {
            item {
                InputField(
                    onButtonClick = { amount, message ->
                        if (amount != 0) {
                            totalMoney += amount
                            coroutineScope.launch {
                                database.insert(
                                    History(
                                        amount = amount,
                                        message = if (message.isBlank()) "No Message" else message,
                                        date = System.currentTimeMillis()
                                    )
                                )
                            }
                        }
                    }
                )
            }
            if (!showHistory){
                item {
                    MoneyGraph(Modifier
                        .size(300.dp)
                        .clip(CircleShape)
                        .clickable { showHistory = !showHistory }, currentMoney = totalMoney)
                }
            } else {
                items(history.value) {
                    HistoryItem(
                        modifier = Modifier.fillMaxWidth(),
                        history = it
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    history: History
) {
    OutlinedCard(modifier, shape = MaterialTheme.shapes.small) {
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