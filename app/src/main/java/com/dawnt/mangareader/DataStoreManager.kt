package com.dawnt.mangareader

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dawnt.mangareader.schemas.ConfigPreferences
import com.dawnt.mangareader.schemas.MangaChapterViewedPreferences
import com.dawnt.mangareader.schemas.MangaPreviewScheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class DataStoreManager {
    companion object {
        private sealed class PreferenceKeys(val key: Preferences.Key<String>) {
            data object Config : PreferenceKeys(stringPreferencesKey("config"))
            data object ChaptersViewed : PreferenceKeys(stringPreferencesKey("chapters_viewed"))
            data object FavoriteMangas : PreferenceKeys(stringPreferencesKey("favorite_mangas"))
            data object DownloadedMangas : PreferenceKeys(stringPreferencesKey("manga_download"))
        }

        private lateinit var dataStore: DataStore<Preferences>
        var currentConfig: ConfigPreferences = ConfigPreferences()
            get() = field.copy()
            private set

        suspend fun init(context: Context) {
            dataStore = PreferenceDataStoreFactory.create {
                File(
                    context.filesDir,
                    "datastore/manga_reader.preferences_pb"
                )
            }

            // Read DataStore Config Preferences no async
            runBlocking {
                dataStore.data.map {
                    val storeConfig = it[PreferenceKeys.Config.key]
                    if (storeConfig != null)
                        currentConfig = Json.decodeFromString(storeConfig)
                }.first()
            }

            dataStore.edit {
                val favorite = it[PreferenceKeys.FavoriteMangas.key]
                val chaptersViewed = it[PreferenceKeys.ChaptersViewed.key]
                if (favorite == null)
                    it[PreferenceKeys.FavoriteMangas.key] = Json.encodeToString(
                        listOf<MangaPreviewScheme>()
                    )
                if (chaptersViewed == null)
                    it[PreferenceKeys.ChaptersViewed.key] = Json.encodeToString(
                        mapOf<String, MangaChapterViewedPreferences>()
                    )
            }
            println(dataStore.data.first())
        }

        suspend fun clearPreferences() {
            dataStore.edit {
                it.clear()
                it[PreferenceKeys.Config.key] = Json.encodeToString(ConfigPreferences())
                it[PreferenceKeys.ChaptersViewed.key] = Json.encodeToString(
                    mutableMapOf<String, MangaChapterViewedPreferences>()
                )
                it[PreferenceKeys.FavoriteMangas.key] = Json.encodeToString(
                    mutableListOf<MangaPreviewScheme>()
                )
                it[PreferenceKeys.DownloadedMangas.key] = Json.encodeToString(
                    mutableListOf<MangaPreviewScheme>()
                )
            }

            println(dataStore.data.first())
        }

        suspend fun saveConfigPreferences(value: ConfigPreferences) {
            currentConfig = value
            dataStore.edit {
                it[PreferenceKeys.Config.key] = Json.encodeToString(value)
            }
        }

        suspend fun getChaptersViewed(
            server: Int,
            nameURL: String
        ): MangaChapterViewedPreferences? {
            val key = "($server).$nameURL"
            return dataStore.data.map {
                val storeMangas = it[PreferenceKeys.ChaptersViewed.key]
                val mangas = Json.decodeFromString<Map<String, MangaChapterViewedPreferences>>(
                    storeMangas!!
                )

                mangas[key]?.let { chapter ->
                    return@map chapter
                }
                return@map null
            }.first()
        }

        suspend fun getAllChaptersViewed(): Map<String, MangaChapterViewedPreferences> {
            return dataStore.data.map {
                val storeMangas = it[PreferenceKeys.ChaptersViewed.key]
                return@map Json.decodeFromString<Map<String, MangaChapterViewedPreferences>>(
                    storeMangas!!
                )
            }.first()
        }

        suspend fun addChaptersViewed(
            server: Int,
            nameURL: String,
            value: MangaChapterViewedPreferences
        ) {
            val key = "($server).$nameURL"
            dataStore.edit {
                val storeMangas = it[PreferenceKeys.ChaptersViewed.key]
                val mangas =
                    Json.decodeFromString<MutableMap<String, MangaChapterViewedPreferences>>(
                        storeMangas!!
                    )

                mangas[key]?.let { manga ->
                    manga.chapters = value.chapters
                    mangas[key] = manga
                } ?: run {
                    mangas[key] = value
                }

                it[PreferenceKeys.ChaptersViewed.key] = Json.encodeToString(mangas)
            }
        }

        suspend fun getFavoriteMangas(): List<MangaPreviewScheme> {
            return dataStore.data.map {
                val storeMangas = it[PreferenceKeys.FavoriteMangas.key]
                return@map Json.decodeFromString<MutableList<MangaPreviewScheme>>(storeMangas!!)
            }.first()
        }

        suspend fun changeFavoriteState(value: MangaPreviewScheme) {
            dataStore.edit {
                val storeMangas = it[PreferenceKeys.FavoriteMangas.key]
                val mangas = Json.decodeFromString<MutableList<MangaPreviewScheme>>(storeMangas!!)
                if (mangas.contains(value)) {
                    mangas.remove(value)
                } else {
                    mangas.add(0, value)
                }

                it[PreferenceKeys.FavoriteMangas.key] = Json.encodeToString(mangas)
            }
        }
    }
}
