package com.example.tripcalculator.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tripcalculator.database.History
import com.example.tripcalculator.database.HistoryDatabase
import com.example.tripcalculator.ui.theme.Green
import com.example.tripcalculator.ui.theme.Red
import kotlinx.coroutines.launch
import kotlin.math.abs


@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val database = remember { HistoryDatabase.getDatabase(context).dao }
    var totalMoney by remember {
        mutableLongStateOf(0)
    }
    LaunchedEffect(Unit) {
        database.getAll().collect {
            totalMoney = it.sumOf { it.amount }
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Column {
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
        InputField(
            onButtonClick = { amount, message ->
                if (amount != 0L) {
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
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            MoneyGraph(
                Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("history_screen")
                    },
                currentMoney = totalMoney
            )
        }
    }
}