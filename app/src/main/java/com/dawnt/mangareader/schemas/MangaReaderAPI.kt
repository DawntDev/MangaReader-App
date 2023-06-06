package com.dawnt.mangareader.schemas

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MangaReaderAPI {
    @Headers("Accept: application/json")

    @GET("api/main-screen")
    fun getMainScreenAsync(): Deferred<Map<String, Array<MangaPreview>>?>?

    @GET("api/manga/details")
    fun getMangaDetailAsync(
        @Query("nameURL") nameURL: String
    ): Deferred<MangaDetails?>?

    @GET("api/manga/chapter")
    fun getMangaChapterAsync(
        @Query("nameURL") nameURL: String,
        @Query("chapter") chapter: String,
        @Query("inBytes") inBytes: Boolean
    ): Deferred<Array<String>?>?

    @GET("api/manga/search")
    fun getMangaSearchAsync(
        @Query("text") text: String,
        @Query("genres") genres: Array<String>
    ): Deferred<Array<MangaDetails>?>?
}
