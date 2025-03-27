package com.example.tripcalculator.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.tripcalculator.ui.theme.Green
import com.example.tripcalculator.ui.theme.Red
import kotlin.math.abs

@Composable
fun MoneyGraph(
    modifier: Modifier = Modifier,
    step: Long = 25000,
    currentMoney: Long
) {
    val totalMoney =
        if (abs(currentMoney) <= step) step else ((abs(currentMoney) + step - 1) / step) * step
    val progress = currentMoney.toDouble() / totalMoney.toDouble() * 360f
    val textMeasurer = rememberTextMeasurer()
    val progressAnimate = remember { Animatable(0f) }
    val textAnimate = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        progressAnimate.animateTo(progress.toFloat(), animationSpec = tween(1000))
    }
    LaunchedEffect(currentMoney) {
        textAnimate.animateTo(currentMoney.toFloat(), animationSpec = tween(1000))
    }
    val textLayoutResult = textMeasurer.measure(
        text = "${textAnimate.value.toInt()}₹",
        style = MaterialTheme.typography.headlineMedium
    )
    Canvas(modifier) {
        drawArc(
            color = Color.LightGray.copy(0.5f),
            startAngle = 270f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(
                size.width / 2 - (size.minDimension - 48.dp.toPx()) / 2,
                size.height / 2 - (size.minDimension - 48.dp.toPx()) /2
            ),
            size = Size(size.minDimension - 48.dp.toPx(), size.minDimension - 48.dp.toPx()),
            style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = if (progressAnimate.value < 0) Red else Green,
            startAngle = 270f,
            sweepAngle = progressAnimate.value,
            useCenter = false,
            topLeft = Offset(
                size.width / 2 - (size.minDimension - 48.dp.toPx()) / 2,
                size.height / 2 - (size.minDimension - 48.dp.toPx()) /2
            ),
            size = Size(size.minDimension - 48.dp.toPx(), size.minDimension - 48.dp.toPx()),
            style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Round)
        )
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                size.width / 2 - textLayoutResult.size.width/2,
                size.height / 2 - textLayoutResult.size.height/2
            ),
            color = if (textAnimate.value < 0) Red else Green
        )
    }
}