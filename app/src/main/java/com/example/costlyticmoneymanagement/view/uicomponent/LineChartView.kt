package com.example.costlyticmoneymanagement.view.uicomponent

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun LineChartView(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    // Replaces ValueAnimator.ofFloat(0f, 1f) with 1200ms duration
    var startAnimation by remember { mutableStateOf(false) }
    val animationProgress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "line_chart_progress"
    )

    LaunchedEffect(data) {
        startAnimation = false
        startAnimation = true
    }

    Canvas(modifier = modifier) {
        if (data.isEmpty() || data.size < 2) return@Canvas

        val canvasWidth = size.width
        val canvasHeight = size.height
        val maxValue = data.maxOrNull() ?: return@Canvas
        if (maxValue <= 0f) return@Canvas

        val topPadding = 20f
        val bottomPadding = 10f
        val chartHeight = canvasHeight - topPadding - bottomPadding
        val spaceX = canvasWidth / (data.size - 1)

        val points = data.mapIndexed { index, value ->
            val x = index * spaceX
            val animatedValue = value * animationProgress
            val y = canvasHeight - bottomPadding - (animatedValue / maxValue) * chartHeight
            Offset(x, y)
        }

        val linePath = Path().apply {
            moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val current = points[i]
                val midX = (prev.x + current.x) / 2f
                cubicTo(midX, prev.y, midX, current.y, current.x, current.y)
            }
        }

        val fillPath = Path().apply {
            moveTo(points[0].x, canvasHeight)
            lineTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val current = points[i]
                val midX = (prev.x + current.x) / 2f
                cubicTo(midX, prev.y, midX, current.y, current.x, current.y)
            }
            lineTo(points.last().x, canvasHeight)
            close()
        }

        // Gradient for fill area (#447B4DFF -> Transparent)
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x447B4DFF),
                    Color.Transparent
                ),
                startY = 0f,
                endY = canvasHeight
            )
        )

        // Gradient for thick rounded line (#7B4DFF -> #C6A7FF)
        drawPath(
            path = linePath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF7B4DFF),
                    Color(0xFFC6A7FF)
                ),
                startY = 0f,
                endY = canvasHeight
            ),
            style = Stroke(
                width = 18f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}