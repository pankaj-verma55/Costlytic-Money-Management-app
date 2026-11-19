package com.example.costlyticmoneymanagement.view.uicomponent

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun ScrollProgressView(
    progress: Float,
    onProgressChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val clampedProgress = progress.coerceIn(0f, 100f)

    val textPaint = remember {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 40f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (size.width > 0) {
                        val percent = ((offset.x / size.width) * 100f).coerceIn(0f, 100f)
                        onProgressChanged(percent)
                    }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    if (size.width > 0) {
                        val percent = ((change.position.x / size.width) * 100f).coerceIn(0f, 100f)
                        onProgressChanged(percent)
                    }
                }
            }
    ) {
        val backgroundStroke = 40f
        val progressStroke = 50f
        val bubbleRadius = 50f
        val horizontalPadding = maxOf(backgroundStroke, bubbleRadius)
        val usableWidth = (size.width - (horizontalPadding * 2f)).coerceAtLeast(0f)

        val centerY = size.height / 2f
        val progressX = horizontalPadding + (clampedProgress / 100f) * usableWidth

        // 1. Background Track (#E0E0E0)
        drawLine(
            color = Color(0xFFE0E0E0),
            start = Offset(horizontalPadding, centerY),
            end = Offset(size.width - horizontalPadding, centerY),
            strokeWidth = backgroundStroke,
            cap = StrokeCap.Round
        )

        // 2. Active Progress Track (#7B3FF2)
        drawLine(
            color = Color(0xFF7B3FF2),
            start = Offset(horizontalPadding, centerY),
            end = Offset(progressX, centerY),
            strokeWidth = progressStroke,
            cap = StrokeCap.Round
        )

        // 3. Floating Bubble Indicator (#7B3FF2)
        drawCircle(
            color = Color(0xFF7B3FF2),
            radius = bubbleRadius,
            center = Offset(progressX, centerY - 60f)
        )

        // 4. Percentage Label inside Bubble
        drawContext.canvas.nativeCanvas.drawText(
            "${clampedProgress.toInt()}%",
            progressX,
            centerY - 45f,
            textPaint
        )
    }
}