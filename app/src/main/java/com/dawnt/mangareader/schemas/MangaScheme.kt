@file:Suppress("PropertyName")

package com.dawnt.mangareader.schemas

import kotlinx.serialization.Serializable

data class MangaScheme(
    val title: String,
    val type_of: String,
    val rating: Float,
    val genres: Array<String>?,
    val overview: String?,
    val server: Int,
    val name_url: String,
    val cover_url: String?,
    val nsfw: Boolean,
    val chapters_list: Array<ChapterScheme>?
)

@Serializable
data class MangaPreviewScheme(
    val title: String,
    val name_url: String,
    val server: Int,
    val cover_url: String?
)

@Serializable
data class ChapterScheme(
    val name: String,
    val url_name: String
)
