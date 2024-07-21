package com.dawnt.mangareader.schemas

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface APIScheme {
    @Headers("Accept: application/json")

    @GET("servers")
    suspend fun getServersAsync(
        @Query("id") id: Int?,
        @Query("name") name: String?
    ): Response<Array<ServerScheme>>

    @POST("servers/fetch")
    suspend fun fetchServersAsync(
        @Body id: Int,
        @Body level: FetchLevel = FetchLevel.Update,
        @Body force: Boolean = false
    ): Response<ServerFetchSchema>

    @GET("api/main-screen")
    suspend fun getMainScreenAsync(): Response<Map<String, Array<MangaPreviewScheme>>>

    @GET("api/genres")
    suspend fun getGenresAsync(): Response<Map<String, String>>

    @GET("api/manga/details")
    suspend fun getMangaDetailAsync(
        @Query("server") server: Int,
        @Query("name_url") nameURL: String
    ): Response<MangaScheme>

    @GET("api/manga/chapter")
    suspend fun getMangaChapterAsync(
        @Query("server") server: Int,
        @Query("name_url") nameURL: String,
        @Query("chapter_url") chapterURL: String
    ): Response<Array<String>>

    @GET("api/manga/search")
    suspend fun getMangaSearchAsync(
        @Query("title") text: String,
        @Query("genres") genres: Array<String>
    ): Response<Array<MangaScheme>>
}
