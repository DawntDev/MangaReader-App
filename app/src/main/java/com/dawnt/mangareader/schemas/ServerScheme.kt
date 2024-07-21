@file:Suppress("PropertyName")
package com.dawnt.mangareader.schemas

data class ServerScheme(
    val name: String,
    val elements: Int,
    val nsfw: Boolean,
    val in_working: Boolean,
)

data class ServerFetchSchema(
    val status: String,
    val websocket: String,
)

enum class FetchLevel {
    Update,
    Scan,
    Hard
}