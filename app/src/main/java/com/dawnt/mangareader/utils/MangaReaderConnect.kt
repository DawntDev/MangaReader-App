package com.dawnt.mangareader.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dawnt.mangareader.schemas.MangaDetails
import com.dawnt.mangareader.schemas.MangaPreview
import com.dawnt.mangareader.schemas.MangaReaderAPI
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class MangaReaderConnect(URL: String) : ViewModel() {
    private var _api: MangaReaderAPI

    private val _mainScreen = MutableLiveData<Map<String, Array<MangaPreview>>?>(null)
    val mainScreen: LiveData<Map<String, Array<MangaPreview>>?> = _mainScreen

    private val _mangaDetail = MutableLiveData<MangaDetails?>(null)
    val mangaDetail: LiveData<MangaDetails?> = _mangaDetail

    private val _mangaChapter = MutableLiveData<Array<String>?>(null)
    val mangaChapter: LiveData<Array<String>?> = _mangaChapter

    private val _mangaSearch = MutableLiveData<Array<MangaDetails>?>(null)
    val mangaSearch: LiveData<Array<MangaDetails>?> = _mangaSearch

    init {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()

        _api = retrofit.create(MangaReaderAPI::class.java)
    }

    fun getMainScreen() {
        _mainScreen.value = null
        viewModelScope.launch {
            try {
                val response: Map<String, Array<MangaPreview>>? = _api.getMainScreenAsync()?.await()
                _mainScreen.value = response
            } catch (fail: SocketTimeoutException) {
                Log.e("RequestFail", "Fail to request MainScreen", fail)
            }
        }
    }

    fun getMangaDetails(nameURL: String) {
        _mangaDetail.value = null
        viewModelScope.launch {
            try {
                val response: MangaDetails? = _api.getMangaDetailAsync(nameURL)?.await()
                _mangaDetail.value = response
            } catch (fail: SocketTimeoutException) {
                Log.e("RequestFail", "Fail to request MangaDetails", fail)
            }
        }
    }

    fun getMangaChapter(
        nameURL: String,
        chapter: String,
        inBytes: Boolean = false
    ) {
        _mangaChapter.value = null
        viewModelScope.launch {
            try {
                val response: Array<String>? =
                    _api.getMangaChapterAsync(nameURL, chapter, inBytes)?.await()
                _mangaChapter.value = response
            } catch (fail: SocketTimeoutException) {
                Log.e("Request Fail", "Fail to request Chapter", fail)
            }
        }
    }

    fun getMangaSearch(text: String = "", genres: Array<String> = arrayOf()) {
        _mangaSearch.value = null
        viewModelScope.launch {
            try {
                val response: Array<MangaDetails>? = _api.getMangaSearchAsync(text, genres)?.await()
                _mangaSearch.value = response
            } catch (fail: SocketTimeoutException) {
                Log.e("Request Fail", "Fail to search")
            }
        }
    }
}
