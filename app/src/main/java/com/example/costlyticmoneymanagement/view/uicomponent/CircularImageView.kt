package com.example.costlyticmoneymanagement.view.uicomponent

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.example.costlyticmoneymanagement.R

/**
 * Helper matching your original drawableToBitmap logic if you ever pass a raw Android Drawable.
 */
fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) return drawable.bitmap

    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

/**
 * Pure Jetpack Compose replacement for your custom CircularImageView.
 *
 * - cornerRadius = null -> Full circle (matches cornerRadius = -1 in XML)
 * - cornerRadius = 12.dp -> Rounded rectangle (matches cornerRadius >= 0 in XML)
 * - bitmap / painter / drawable / resId all null -> Clears/removes image (replaces removeImage())
 */
@Composable
fun CircularImageView(
    modifier: Modifier = Modifier,
    bitmap: Bitmap? = null,
    painter: Painter? = null,
    drawable: Drawable? = null,
    @DrawableRes resId: Int? = null,
    contentDescription: String? = null,
    cornerRadius: Dp? = null,
    borderWidth: Dp = 0.dp,
    borderColor: Color = colorResource(id = R.color.accent_color),
    contentScale: ContentScale = ContentScale.Crop
) {
    // 1. Shape calculation (Circle vs RoundedCorner)
    val shape = if (cornerRadius != null && cornerRadius >= 0.dp) {
        RoundedCornerShape(cornerRadius)
    } else {
        CircleShape
    }

    // 2. Border drawn after clipping so it never gets cut off
    val borderModifier = if (borderWidth > 0.dp) {
        Modifier.border(width = borderWidth, color = borderColor, shape = shape)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(shape)
            .then(borderModifier)
    ) {
        when {
            bitmap != null -> {
                val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }
                Image(
                    bitmap = imageBitmap,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
            drawable != null -> {
                val imageBitmap = remember(drawable) { drawableToBitmap(drawable).asImageBitmap() }
                Image(
                    bitmap = imageBitmap,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
            painter != null -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
            resId != null -> {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // If all image sources are null, renders empty (equivalent to removeImage())
        }
    }
}