package com.dawnt.mangareader.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.DataStoreManager
import com.dawnt.mangareader.R
import com.dawnt.mangareader.components.CoilImage
import com.dawnt.mangareader.components.GridImages
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.schemas.ChapterScheme
import com.dawnt.mangareader.schemas.MangaChapterViewedPreferences
import com.dawnt.mangareader.schemas.MangaPreviewScheme
import com.dawnt.mangareader.schemas.MangaScreens
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import kotlin.math.roundToInt

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

            item {
                GridImages(
                    items = favoriteMangas.toTypedArray(),
                    navController = navController
                )

            }

            item {
                Header(title = "Last Chapters Read", subtitle = "Pick up where you left off!")
            }

            items(chapterViewed.toList()) {
                val (nameURL, manga) = it
                LastChapterViewed(
                    navController = navController,
                    server = manga.server,
                    title = manga.title,
                    nameURL = nameURL.split("(${manga.server}).").last(),
                    coverURL = manga.coverURL,
                    chapter = manga.chapters.last()
                )
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

@Composable
private fun LastChapterViewed(
    navController: NavController,
    server: Int,
    title: String,
    nameURL: String,
    coverURL: String?,
    chapter: ChapterScheme,
) {
    val apiClient = APIClient.getInstance()
    var swiped by remember { mutableStateOf(false) }
    LaunchedEffect(swiped) {
        if (swiped) {
            DataStoreManager.changeVisibility(server, nameURL, false)
        }
    }

    val unfollow = SwipeAction(
        onSwipe = { swiped = true },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.outline_close_24),
                contentDescription = "Close Icon",
                tint = onBackground
            )
        },
        background = Color(0xFFB63D3D),
    )
    AnimatedVisibility(
        visible = !swiped,
        exit = slideOutHorizontally(targetOffsetX = { n -> (n * -1.5f).roundToInt() })
    ) {
        SwipeableActionsBox(endActions = listOf(unfollow), swipeThreshold = 72.dp) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .fillMaxWidth()
                    .height(86.dp)
                    .background(secondaryBackground)
                    .clickable {
                        apiClient.getMangaDetails(server, nameURL)
                        apiClient.getMangaChapter(server, nameURL, chapter.url_name)
                        navController.navigate(
                            MangaScreens.MangaChapter.route
                                    + "?server=${server}"
                                    + "&nameURL=${nameURL}"
                                    + "&chapterName=${chapter.name}"
                                    + "&chapterURL=${chapter.url_name}"
                                    + "&fromUserView=true"
                        )
                    }
            ) {
                CoilImage(
                    url = coverURL,
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
                        text = chapter.name,
                        color = onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = Dosis,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            lineHeight = 16.sp,
                            letterSpacing = 0.5.sp,
                        )
                    )
                    Text(
                        text = title,
                        color = onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = Dosis,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,
                            letterSpacing = 0.5.sp
                        )
                    )

                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun UserPrev() {
    User(rememberNavController())
}