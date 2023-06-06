package com.dawnt.mangareader.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dawnt.mangareader.MainActivity

@Composable
fun loadImage(URL: String?): MutableState<Bitmap?> {
    val defaultImageBitmap = MainActivity.Assets.defaultImageBitmap
    val bitmapState: MutableState<Bitmap?> = remember { mutableStateOf(defaultImageBitmap) }

    Glide.with(LocalContext.current)
        .asBitmap()
        .load(URL.toString())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .timeout(1000 * 3600)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                bitmapState.value = defaultImageBitmap
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                TODO("Not yet implemented")
            }
        })
    return bitmapState
}
