package com.dawnt.mangareader.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.components.Loader
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.components.SearchBar
import com.dawnt.mangareader.components.SearchItem
import com.dawnt.mangareader.ui.theme.Background

@Composable
fun Library(
    navController: NavController,
    APIConn: MangaReaderConnect,
    genres: Map<String, Color>
) {
    val viewModel: MangaReaderConnect = APIConn
    val data by viewModel.mangaSearch.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        SearchBar(
            APIConn = APIConn,
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
                        APIConn = APIConn,
                        title = el.title,
                        typeOf = el.typeOf,
                        rating = el.rating,
                        genres = genres.filterKeys { genre -> genre in el.genre },
                        nameURL = el.nameURL,
                        coverURL = el.coverURL
                    )
                }
                item { Spacer(modifier = Modifier.height(64.dp)) }
            }
        } ?: run { Loader(modifier = Modifier.align(Alignment.Center)) }

        NavBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}