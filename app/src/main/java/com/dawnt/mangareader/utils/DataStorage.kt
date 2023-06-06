package com.dawnt.mangareader.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dawnt.mangareader.schemas.MangaChapterViewed
import com.dawnt.mangareader.schemas.MangaPreview
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import java.io.File

object DataStorage {
    private lateinit var dataStore: DataStore<Preferences>
    private val CHAPTERS_VIEWED_KEY = stringPreferencesKey("chapters_viewed")
    private val FAVORITE_MANGAS_KEY = stringPreferencesKey("favorite_mangas")
    private val CHAPTERS_DOWNLOADED_KEY = stringPreferencesKey("chapters_downloaded")

    var currentChaptersViewed: MutableMap<String, MangaChapterViewed> = mutableMapOf()
    var currentFavoriteMangas: MutableList<MangaPreview> = mutableListOf()

    suspend fun init(context: Context) {
        dataStore = PreferenceDataStoreFactory.create {
            File(context.filesDir, "datastore/manga_reader.preferences_pb")
        }
        currentChaptersViewed = getChaptersViewed()
        currentFavoriteMangas = getFavoriteMangas()
    }

    suspend fun saveVisibility(key: String, visibility: Boolean) {
        currentChaptersViewed[key]?.visible = visibility
        val json = Gson().toJson(currentChaptersViewed)
        dataStore.edit { preferences ->
            preferences[CHAPTERS_VIEWED_KEY] = json
        }
    }

    suspend fun saveChaptersViewed(key: String, chapterViewed: String) {
        println("saveChapterViewed(key = $key, chapterViewed = $chapterViewed)")
        currentChaptersViewed[key]?.chapters?.add(chapterViewed)
        val json = Gson().toJson(currentChaptersViewed)
        dataStore.edit { preferences ->
            preferences[CHAPTERS_VIEWED_KEY] = json
        }
    }

    suspend fun saveChaptersViewed(
        key: String,
        title: String,
        coverURL: String,
        chapterViewed: String
    ) {
        println("saveChapterViewed(\n\tkey = $key, \n\ttitle = $title, \n\tcoverURL = $coverURL, \n\tchapterViewed = $chapterViewed\n)")
        currentChaptersViewed[key] = MangaChapterViewed(
            title,
            coverURL,
            mutableListOf(chapterViewed),
            true
        )

        val json = Gson().toJson(currentChaptersViewed)
        dataStore.edit { preferences ->
            preferences[CHAPTERS_VIEWED_KEY] = json
        }
    }

    private suspend fun getChaptersViewed(): MutableMap<String, MangaChapterViewed> {
        val json = dataStore.data.first()[CHAPTERS_VIEWED_KEY] ?: "{}"
        return Gson().fromJson(
            json,
            object : TypeToken<MutableMap<String, MangaChapterViewed>>() {}.type
        )
    }

    suspend fun saveFavoriteMangas() {
        val json = Gson().toJson(currentFavoriteMangas)
        dataStore.edit { preferences ->
            preferences[FAVORITE_MANGAS_KEY] = json
        }
    }

    private suspend fun getFavoriteMangas(): MutableList<MangaPreview> {
        val json = dataStore.data.first()[FAVORITE_MANGAS_KEY] ?: "[]"
        return Gson().fromJson(json, object : TypeToken<MutableList<MangaPreview>>() {}.type)
    }

}