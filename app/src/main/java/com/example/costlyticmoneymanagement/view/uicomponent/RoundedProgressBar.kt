package com.example.costlyticmoneymanagement.view.uicomponent

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun RoundedProgressBar(
    progress: Float,
    progressColor: Color,
    modifier: Modifier = Modifier
) {
    val clampedProgress = progress.coerceIn(0f, 100f)

    var startAnim by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnim) clampedProgress else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "rounded_progress"
    )

    LaunchedEffect(clampedProgress) {
        startAnim = true
    }

    Canvas(modifier = modifier) {
        val radius = size.height / 2f

        // Background track (#E0E0E0)
        drawRoundRect(
            color = Color(0xFFE0E0E0),
            size = size,
            cornerRadius = CornerRadius(radius, radius)
        )

        // Foreground animated progress fill
        val progressWidth = (animatedProgress / 100f) * size.width
        if (progressWidth > 0f) {
            drawRoundRect(
                color = progressColor,
                size = Size(width = progressWidth, height = size.height),
                cornerRadius = CornerRadius(radius, radius)
            )
        }
    }
}