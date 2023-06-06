package com.dawnt.mangareader.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dawnt.mangareader.components.AnimatedCounter
import com.dawnt.mangareader.components.GridImages
import com.dawnt.mangareader.components.LastChapterViewed
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.utils.DataStorage
import com.dawnt.mangareader.utils.MangaReaderConnect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun User(navController: NavController, APIConn: MangaReaderConnect) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(modifier = Modifier.padding(top = 28.dp, start = 8.dp, end = 8.dp)) {
            stickyHeader {
                Header(
                    title = "Hello Reader!",
                    subtitle = "A little bit of you."
                )
            }
            item {
                val animDuration = 3500
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ItemStats(
                        text = "Favorite Mangas",
                        number = DataStorage.currentFavoriteMangas.size,
                        animDuration = animDuration - 100,
                        delay = 150
                    )
                    ItemStats(
                        text = "Mangas Read",
                        number = DataStorage.currentChaptersViewed.size,
                        animDuration = animDuration - 200,
                        delay = 300
                    )
                    ItemStats(
                        text = "Chapters Read",
                        number = DataStorage.currentChaptersViewed.values.sumOf { it.chapters.size },
                        animDuration = animDuration - 300,
                        delay = 450
                    )
                }
            }
            stickyHeader {
                Header(
                    title = "Chapters Viewed",
                    subtitle = "Pick up where you left off!"
                )
            }
            if (DataStorage.currentChaptersViewed.isEmpty()) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    ) {
                        Text(
                            text = "No chapters read",
                            color = onBackground,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontFamily = Dosis,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            } else {
                val regex = Regex("[0-9._-]+")
                items(DataStorage.currentChaptersViewed.toList()) { (key, manga) ->
                    val chapters = manga.chapters
                    val floatChapters = chapters.mapNotNull {
                        regex.find(it)?.value?.let { res ->
                            val symbols = listOf(".", "-", "_")
                            var string = if (symbols.contains("${res.first()}"))
                                res.substring(1)
                            else if (symbols.contains("${res.last()}"))
                                res.substring(0, res.length - 1)
                            else
                                res
                            string = string.replace(regex = Regex("[_\\-]"), ".")
                            return@mapNotNull string.toFloatOrNull()
                        }
                    }
                    if (manga.visible) {
                        LastChapterViewed(
                            navController = navController,
                            APIConn = APIConn,
                            title = manga.title,
                            chapter = chapters[floatChapters.indexOf(floatChapters.max())],
                            nameURL = key,
                            coverURL = manga.coverURL
                        )
                    }
                }
            }
            stickyHeader {
                Header(
                    title = "Favorite Mangas",
                    subtitle = "Re-read what he has marked for you."
                )
            }
            item {
                if (DataStorage.currentFavoriteMangas.isEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    ) {
                        Text(
                            text = "No mangas marked as favorites",
                            color = onBackground,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontFamily = Dosis,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                } else {
                    GridImages(
                        items = DataStorage.currentFavoriteMangas.toTypedArray(),
                        navController = navController,
                        APIConn = APIConn
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(56.dp)) }
        }
        NavBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun Header(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            color = onBackground,
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
        )
        Text(text = subtitle, color = onBackground)
    }
}


@Composable
private fun ItemStats(text: String, number: Int, animDuration: Int, delay: Long = 0) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedCounter(
            target = number,
            animDuration = animDuration,
            color = Primary,
            delay = delay,
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 37.sp,
                letterSpacing = 0.5.sp,
                lineHeight = 16.sp
            )
        )
        Text(
            text = text,
            color = onBackground.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 0.5.sp
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun UserPrev() {
    User(rememberNavController(), MangaReaderConnect("https://google.com"))
}