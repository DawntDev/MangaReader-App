package com.dawnt.mangareader.schemas

import androidx.compose.ui.graphics.FilterQuality
import kotlinx.serialization.Serializable

enum class Quality(val filterQuality: FilterQuality) {
    None(FilterQuality.None),
    Low(FilterQuality.Low),
    Medium(FilterQuality.Medium),
    High(FilterQuality.High)
}

enum class DownloadQuality(val percentage: Float) {
    Low(0.25f),
    Normal(0.5f),
    Medium(0.75f),
    High(1.0f)
}

@Serializable
data class ConfigPreferences(
    var baseURL: String = "",
    var chapterRenderingQuality: Quality = Quality.Medium,
    var downloadQuality: DownloadQuality = DownloadQuality.Medium,
    var nsfw: Boolean = false
)

@Serializable
data class MangaChapterViewedPreferences(
    val title: String,
    val coverURL: String?,
    var chapters: MutableList<ChapterScheme>,
)


@Serializable
data class DownloadMangaPreferences(
    val nameURL: String,
    val chapterURL: String,
    val coverURL: String?,
    val server: Int,
    val chapterIndex: Array<ChapterScheme>
)