package com.dawnt.mangareader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.R
import com.dawnt.mangareader.screens.MangaScreens
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground

@Composable
fun ChapterNavBar(
    navController: NavController,
    APIConn: MangaReaderConnect,
    nameURL: String,
    currentChapter: String,
    chapters: Array<String>,
    fromUserView: Boolean
) {
    val index = chapters.indexOf(currentChapter)
    val prevChapter = chapters.getOrNull(index - 1)
    val nextChapter = chapters.getOrNull(index + 1)


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(secondaryBackground)
    ) {
        nextChapter?.let {
            TextButton(
                onClick = {
                    APIConn.getMangaChapter(nameURL, nextChapter)
                    navController.popBackStack()
                    navController.navigate(
                        MangaScreens.MangaChapter.route
                                + "?nameURL=${nameURL}"
                                + "&chapter=$nextChapter"
                                + "&fromUserView=$fromUserView"
                    )
                }, modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_24),
                        contentDescription = "Previous Chapter",
                        tint = onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                    ChapterLabel(text = it)
                }
            }
        } ?: run {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }

        prevChapter?.let {
            TextButton(
                onClick = {
                    APIConn.getMangaChapter(nameURL, prevChapter)
                    navController.popBackStack()
                    navController.navigate(
                        MangaScreens.MangaChapter.route
                                + "?nameURL=$nameURL"
                                + "&chapter=$prevChapter"
                                + "&fromUserView=$fromUserView"
                    )
                },
                Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ChapterLabel(text = it)
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_24),
                        contentDescription = "Next Chapter",
                        tint = onBackground,
                        modifier = Modifier
                            .scale(-1f)
                            .size(32.dp)
                    )

                }
            }
        } ?: run {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}


@Composable
private fun ChapterLabel(text: String) {
    Text(
        text = "Chapter " + text.replace(regex = Regex("[_\\-]"), "."),
        color = onBackground,
        style = TextStyle(
            fontFamily = Dosis,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.75.sp
        )
    )
}
