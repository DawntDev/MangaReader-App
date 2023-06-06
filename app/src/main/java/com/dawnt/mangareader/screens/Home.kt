package com.dawnt.mangareader.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.components.GridImages
import com.dawnt.mangareader.components.ImageCarousel
import com.dawnt.mangareader.components.Loader
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground


@Composable
fun Home(navController: NavController, APIConn: MangaReaderConnect) {
    val viewModel: MangaReaderConnect = APIConn
    val data by viewModel.mainScreen.observeAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        data?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, start = 8.dp, end = 8.dp)
                    .verticalScroll(scrollState)
            ) {
                for ((key, value) in it) {
                    if (key == "carousel") ImageCarousel(value, APIConn, navController)
                    else {

                        Text(
                            text = key,
                            color = onBackground,
                            modifier = Modifier.padding(vertical = 12.dp),
                            style = TextStyle(
                                fontFamily = Dosis,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                        GridImages(
                            items = value,
                            navController = navController,
                            APIConn = APIConn
                        )

                    }
                }
                Spacer(modifier = Modifier.height(56.dp))
            }
        } ?: run { Loader(modifier = Modifier.align(Alignment.Center)) }
        NavBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
