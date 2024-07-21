package com.dawnt.mangareader

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dawnt.mangareader.schemas.APIScheme
import com.dawnt.mangareader.schemas.MangaPreviewScheme
import com.dawnt.mangareader.schemas.MangaScheme
import com.dawnt.mangareader.schemas.ServerScheme
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient private constructor() : ViewModel() {
    companion object {
        @Volatile
        private var instance: APIClient? = null

        private lateinit var request: APIScheme
        private lateinit var retrofit: Retrofit
        var isBuilt: Boolean = false
            private set

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: APIClient().also { instance = it }
            }

        private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        fun build(): Boolean {
            val baseUrl = DataStoreManager.currentConfig.baseURL
            try {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(okHttpClient)
                    .build()

                request = retrofit.create(APIScheme::class.java)
                isBuilt = true
                return true
            } catch (e: Exception) {
                Log.e("APIClient", e.message.toString())
                isBuilt = false
                return false
            }
        }
    }

    private val _requestError = MutableLiveData<String?>(null)
    val requestError: LiveData<String?> = _requestError
    private fun <T> requestIntegrity(func: suspend () -> T): T? {
        _requestError.postValue(null)
        var data: T? = null

        if (isBuilt) {
            viewModelScope.launch {
                try {
                    data = func()
                } catch (e: Exception) {
                    _requestError.postValue("Function: ${func.javaClass.enclosingMethod?.name}\nError:\n\t${e::class.simpleName}\n\t${e.message}")
                    Log.e("APIClient", e.message.toString())
                }
            }
        } else
            Log.e("APIClient", "Not Built")

        return data
    }

    private val _servers = MutableLiveData<Array<ServerScheme>?>(null)
    val servers: LiveData<Array<ServerScheme>?> = _servers
    fun getServers(id: Int? = null, name: String? = null) = requestIntegrity {
        _servers.postValue(null)
        val response = request.getServersAsync(id, name)
        if (response.body() != null)
            _servers.postValue(response.body())
    }

    fun getGenres(): Map<String, String>? = requestIntegrity {
        return@requestIntegrity runBlocking {
            val response = request.getGenresAsync()
            return@runBlocking response.body()
        }
    }

    private val _mainScreen = MutableLiveData<Map<String, Array<MangaPreviewScheme>>?>(null)
    val mainScreen: LiveData<Map<String, Array<MangaPreviewScheme>>?> = _mainScreen
    fun getMainScreen() = requestIntegrity {
        _mainScreen.postValue(null)
        val response = request.getMainScreenAsync()
        if (response.body() != null)
            _mainScreen.postValue(response.body())
    }

    private val _mangaDetail = MutableLiveData<MangaScheme?>(null)
    val mangaDetail: LiveData<MangaScheme?> = _mangaDetail
    fun getMangaDetails(server: Int, nameURL: String) = requestIntegrity {
        _mangaDetail.postValue(null)
        val response = request.getMangaDetailAsync(server, nameURL)
        if (response.body() != null)
            _mangaDetail.postValue(response.body())
    }

    private val _mangaSearch = MutableLiveData<Array<MangaScheme>?>(null)
    val mangaSearch: LiveData<Array<MangaScheme>?> = _mangaSearch
    fun getMangaSearch(text: String, genres: Array<String>) = requestIntegrity {
        _mangaSearch.postValue(null)
        val response = request.getMangaSearchAsync(text, genres)
        if (response.body() != null)
            _mangaSearch.postValue(response.body())
    }

    private val _mangaChapter = MutableLiveData<Array<String>?>(null)
    val mangaChapter: LiveData<Array<String>?> = _mangaChapter
    fun getMangaChapter(server: Int, nameURL: String, chapterURL: String) = requestIntegrity {
        _mangaChapter.postValue(null)
        val response = request.getMangaChapterAsync(server, nameURL, chapterURL)
        if (response.body() != null)
            _mangaChapter.postValue(response.body())
    }

}