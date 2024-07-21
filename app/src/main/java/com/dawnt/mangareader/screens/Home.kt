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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.components.GridImages
import com.dawnt.mangareader.components.ImageCarousel
import com.dawnt.mangareader.components.Loader
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground


@Composable
fun Home(navController: NavController) {
    val viewModel: APIClient = APIClient.getInstance()
    val data by viewModel.mainScreen.observeAsState()
    val error by viewModel.requestError.observeAsState()
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
                    if (key == "carousel")
                        ImageCarousel(value, navController)
                    else {
                        Text(
                            text = key.replaceFirstChar(Char::uppercaseChar),
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
                            navController = navController
                        )

                    }
                }
                Spacer(modifier = Modifier.height(56.dp))
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

