package com.dawnt.mangareader.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.R
import com.dawnt.mangareader.screens.MangaScreens
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground
import com.dawnt.mangareader.utils.DataStorage
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.utils.loadImage
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import kotlin.math.roundToInt

@Composable
fun LastChapterViewed(
    navController: NavController,
    APIConn: MangaReaderConnect,
    title: String,
    chapter: String,
    nameURL: String,
    coverURL: String
) {
    var swiped by remember { mutableStateOf(false) }
    LaunchedEffect(swiped) {
        if (swiped) {
            DataStorage.saveVisibility(nameURL, false)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .background(secondaryBackground)
                    .clickable {
                        APIConn.getMangaDetails(nameURL)
                        APIConn.getMangaChapter(nameURL, chapter)
                        navController.navigate(
                            MangaScreens.MangaChapter.route
                                    + "?nameURL=$nameURL"
                                    + "&chapter=$chapter"
                                    + "&fromUserView=true"
                        )
                    }
            ) {
                val image = loadImage(URL = coverURL)
                image.value?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Cover Manga",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .width(78.dp)
                            .fillMaxHeight()
                    )
                }
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = title,
                        color = onBackground,
                        style = TextStyle(
                            fontFamily = Dosis,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 280.dp)
                    )
                    Text(
                        text = "Chapter " + chapter.replace(regex = Regex("[_\\-]"), "."),
                        color = onBackground,
                        style = TextStyle(
                            fontFamily = Dosis,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }
}