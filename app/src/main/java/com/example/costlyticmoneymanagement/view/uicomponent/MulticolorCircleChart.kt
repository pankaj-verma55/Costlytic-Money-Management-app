package com.example.costlyticmoneymanagement.view.uicomponent

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.model.data.CircleChartItem

@Composable
fun MulticolorCircleChart(
    items: List<CircleChartItem>,
    modifier: Modifier = Modifier
) {
    val totalValue = remember(items) {
        items.sumOf { it.value.toDouble() }.toFloat()
    }

    var startAnim by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "circle_chart_progress"
    )

    LaunchedEffect(items) {
        startAnim = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (totalValue <= 0f) return@Canvas

            val strokeWidth = 60f
            val padding = strokeWidth
            val arcDiameterWidth = size.width - (padding * 2f)
            val arcDiameterHeight = size.height - (padding * 2f)

            if (arcDiameterWidth <= 0f || arcDiameterHeight <= 0f) return@Canvas

            var startAngle = -90f
            items.forEach { item ->
                val sweepAngle = (item.value / totalValue) * 360f * animatedProgress
                drawArc(
                    color = Color(item.color),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(padding, padding),
                    size = Size(arcDiameterWidth, arcDiameterHeight),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                startAngle += sweepAngle
            }
        }

        // Exact center text from drawCenterText()
        Text(
            text = "$${totalValue.toInt()}",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}