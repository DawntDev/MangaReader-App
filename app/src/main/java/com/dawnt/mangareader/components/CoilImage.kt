package com.dawnt.mangareader.components

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.dawnt.mangareader.R


@Composable
fun CoilImage(
    url: String?,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    quality: FilterQuality = FilterQuality.None
) {
    val uri = Uri.parse(url ?: "").buildUpon().scheme("https").build().toString()
    val defaultImage = painterResource(R.drawable.default_image)
    println(uri)

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .crossfade(true)
            .allowHardware(false)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        error = defaultImage,
        placeholder = defaultImage,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        filterQuality = quality
    )
}
