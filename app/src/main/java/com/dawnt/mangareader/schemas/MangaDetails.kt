package com.dawnt.mangareader.schemas

data class MangaDetails(
    val title: String,
    val typeOf: String,
    val rating: Float,
    val genre: Array<String>,
    val overview: String,
    val nameURL: String,
    val coverURL: String?,
    val chapterList: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MangaDetails

        if (!genre.contentEquals(other.genre)) return false
        if (!chapterList.contentEquals(other.chapterList)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = genre.contentHashCode()
        result = 31 * result + chapterList.contentHashCode()
        return result
    }
}

