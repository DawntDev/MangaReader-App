package com.dawnt.mangareader.schemas

data class MangaChapterViewed(
    val title: String,
    val coverURL: String,
    var chapters: MutableList<String>,
    var visible: Boolean
)
