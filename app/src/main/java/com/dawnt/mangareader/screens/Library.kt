package com.dawnt.mangareader.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.components.Loader
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.components.SearchBar
import com.dawnt.mangareader.components.SearchItem
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Composable
fun Library(navController: NavController) {
    val viewModel: APIClient = APIClient.getInstance()
    val data by viewModel.mangaSearch.observeAsState()
    val error by viewModel.requestError.observeAsState()
    var genres: Map<String, Color> by remember { mutableStateOf(emptyMap()) }
    val context = LocalContext.current

    LaunchedEffect(context) {
        val file = File(context.filesDir, "genres.json")
        if (file.exists()) {
            context.openFileInput("genres.json").bufferedReader().use {
                genres = Json.decodeFromString<Map<String, String>>(it.readText()).mapValues { el ->
                    Color(AndroidColor.parseColor(el.value))
                }

            }
        } else {
            val response: Map<String, String>? = APIClient.getInstance().getGenres()
            if (response != null) {
                context.openFileOutput("genres.json", Context.MODE_PRIVATE).use {
                    it.write(Json.encodeToString(response).toByteArray())
                }
                genres = response.mapValues {
                    Color(AndroidColor.parseColor(it.value))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        SearchBar(
            genres = genres,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        data?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(-1f)
                    .padding(top = 96.dp)
            ) {
                items(it) { el ->
                    SearchItem(
                        navController = navController,
                        item = el,
                        genres = genres.filterKeys { genre -> el.genres?.contains(genre)!! },
                    )
                }
                item { Spacer(modifier = Modifier.height(64.dp)) }
            }
        } ?: run {
            if (error != null) {
                Text(
                    text = error!!,
                    color = Primary,
                    style = TextStyle(
                        fontFamily = Dosis,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Justify
                    ),
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 40.dp)
                        .align(Alignment.Center)
                )
            } else {
                Loader(modifier = Modifier.align(Alignment.Center))
            }
        }

        NavBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}