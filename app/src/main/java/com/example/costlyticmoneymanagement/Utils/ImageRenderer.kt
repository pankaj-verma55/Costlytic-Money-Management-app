package com.example.costlyticmoneymanagement.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.view.uicomponent.CircularImageView
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.URL

object ImageRenderer {
    private const val TAG = "ImageRenderer"

    /**
     * Decodes a Base64 string into a Bitmap safely.
     */
    fun decodeBase64ToBitmap(base64: String?): Bitmap? {
        if (base64.isNullOrEmpty()) return null
        return try {
            val decodedString = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            Log.e(TAG, "Base64 decoding failed", e)
            null
        }
    }

    /**
     * Validates a video URL using RxJava (Exact original logic preserved).
     */
    fun isValidVideo(url: String): Single<Boolean> {
        return Single.create { emitter ->
            try {
                val urlVideo = URL(url)
                if (urlVideo.openConnection().content != null) {
                    emitter.onSuccess(true)
                } else {
                    emitter.onSuccess(false)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
    }
}

/**
 * Renders a Base64 string into a standard Compose Image.
 * Replaces: ImageRenderer.renderBase64(imageView: ImageView, base64: String?)
 */
@Composable
fun RenderBase64Image(
    base64: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val bitmap = remember(base64) { ImageRenderer.decodeBase64ToBitmap(base64) }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_img_placeholder),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

/**
 * Renders a Base64 string into your Compose CircularImageView.
 * Replaces: ImageRenderer.renderBase64(imageView: CircularImageView, base64: String?)
 */
@Composable
fun RenderBase64CircularImage(
    base64: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    cornerRadius: Dp? = null,
    borderWidth: Dp = 0.dp,
    borderColor: Color = colorResource(id = R.color.accent_color),
    contentScale: ContentScale = ContentScale.Crop
) {
    val bitmap = remember(base64) { ImageRenderer.decodeBase64ToBitmap(base64) }

    CircularImageView(
        modifier = modifier,
        bitmap = bitmap,
        painter = if (bitmap == null) painterResource(id = R.drawable.ic_img_placeholder) else null,
        contentDescription = contentDescription,
        cornerRadius = cornerRadius,
        borderWidth = borderWidth,
        borderColor = borderColor,
        contentScale = contentScale
    )
}

/**
 * Loads an image URL via Glide into a standard Compose Image.
 * Replaces: ImageRenderer.renderUrl(...) & ImageRenderer.renderImage(..., ImageView)
 */
@Composable
fun RenderUrlImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    var loadedBitmap by remember(imageUrl) { mutableStateOf<Bitmap?>(null) }

    DisposableEffect(imageUrl) {
        val target = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                loadedBitmap = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                loadedBitmap = null
            }
        }

        try {
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .placeholder(R.drawable.ic_img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(target)
        } catch (e: Exception) {
            Log.e("ImageRenderer", "Glide bitmap loading error", e)
        }

        onDispose {
            Glide.with(context).clear(target)
        }
    }

    if (loadedBitmap != null) {
        Image(
            bitmap = loadedBitmap!!.asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_img_placeholder),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

/**
 * Loads an image URL via Glide into your Compose CircularImageView.
 * Replaces: ImageRenderer.renderImage(..., CircularImageView)
 */
@Composable
fun RenderCircularUrlImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    cornerRadius: Dp? = null,
    borderWidth: Dp = 0.dp,
    borderColor: Color = colorResource(id = R.color.accent_color),
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    var loadedBitmap by remember(imageUrl) { mutableStateOf<Bitmap?>(null) }

    DisposableEffect(imageUrl) {
        val target = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                loadedBitmap = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                loadedBitmap = null
            }
        }

        try {
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .placeholder(R.drawable.ic_img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(target)
        } catch (e: Exception) {
            Log.e("ImageRenderer", "Glide circular bitmap loading error", e)
        }

        onDispose {
            Glide.with(context).clear(target)
        }
    }

    CircularImageView(
        modifier = modifier,
        bitmap = loadedBitmap,
        painter = if (loadedBitmap == null) painterResource(id = R.drawable.ic_img_placeholder) else null,
        contentDescription = contentDescription,
        cornerRadius = cornerRadius,
        borderWidth = borderWidth,
        borderColor = borderColor,
        contentScale = contentScale
    )
}