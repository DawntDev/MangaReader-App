package com.dawnt.mangareader.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.DataStoreManager
import com.dawnt.mangareader.components.CoilImage
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.schemas.MangaChapterViewedPreferences
import com.dawnt.mangareader.schemas.MangaPreviewScheme
import com.dawnt.mangareader.schemas.MangaScreens
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground

@Composable
fun User(navController: NavController) {
    var chapterViewed by remember {
        mutableStateOf<Map<String, MangaChapterViewedPreferences>>(
            mapOf()
        )
    }

    var favoriteMangas by remember {
        mutableStateOf<List<MangaPreviewScheme>>(
            listOf()
        )
    }

    LaunchedEffect(chapterViewed) {
        chapterViewed = DataStoreManager.getAllChaptersViewed()
        favoriteMangas = DataStoreManager.getFavoriteMangas()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 28.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth()
        ) {
            item {
                Text(
                    text = "Hello Reader!",
                    modifier = Modifier.padding(bottom = 12.dp),
                    color = Primary,
                    style = TextStyle(
                        fontFamily = Dosis,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 36.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp
                    )
                )
            }

            item {
                Header(
                    title = "Favorite Mangas",
                    subtitle = "Re-read what he has marked for you."
                )
            }

            items(favoriteMangas) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(86.dp)
                        .background(secondaryBackground)
                        .clickable {
                            APIClient.getInstance().getMangaDetails(it.server, it.name_url)
                            navController.navigate(MangaScreens.MangaDetails.route)
                        }
                ) {
                    CoilImage(
                        url = it.cover_url,
                        modifier = Modifier
                            .width(58.dp)
                            .fillMaxHeight()
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = it.title,
                            color = onBackground,
                            style = TextStyle(
                                fontFamily = Dosis,
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp,
                            )
                        )

                    }
                }
            }

            item {
                Header(title = "Last Chapters Read", subtitle = "Pick up where you left off!")
            }

            items(chapterViewed.toList()) {
                val (nameURL, manga) = it
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(86.dp)
                        .background(secondaryBackground)
                ) {
                    CoilImage(
                        url = manga.coverURL,
                        modifier = Modifier
                            .width(58.dp)
                            .fillMaxHeight()
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = manga.chapters.last().name,
                            color = onBackground,
                            style = TextStyle(
                                fontFamily = Dosis,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 28.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp,
                            )
                        )
                        Text(
                            text = manga.title,
                            color = onBackground,
                            style = TextStyle(
                                fontFamily = Dosis,
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp,
                            )
                        )

                    }
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
    Text(
        text = title,
        color = onBackground,
        style = TextStyle(
            fontFamily = Dosis,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    )
    Text(
        text = subtitle,
        color = onBackground,
        modifier = Modifier.padding(bottom = 12.dp),
        style = TextStyle(
            fontFamily = Dosis,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    )
}

@Preview(showSystemUi = true)
@Composable
fun UserPrev() {
    User(rememberNavController())
}