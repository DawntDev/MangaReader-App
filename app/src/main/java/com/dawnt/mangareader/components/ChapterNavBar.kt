package com.dawnt.mangareader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.R
import com.dawnt.mangareader.schemas.ChapterScheme
import com.dawnt.mangareader.schemas.MangaScreens
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground

@Composable
fun ChapterNavBar(
    navController: NavController,
    server: Int,
    nameURL: String,
    currentChapter: ChapterScheme,
    chapters: Array<ChapterScheme>,
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
                    APIClient.getInstance().getMangaChapter(server, nameURL, nextChapter.url_name)
                    navController.popBackStack()
                    navController.navigate(
                        MangaScreens.MangaChapter.route
                                + "?server=$server"
                                + "&nameURL=$nameURL"
                                + "&chapterName=${nextChapter.name}"
                                + "&chapterURL=${nextChapter.url_name}"
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
                    ChapterLabel(text = it.name)
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
                    APIClient
                        .getInstance()
                        .getMangaChapter(server, nameURL, prevChapter.url_name)
                    navController.popBackStack()
                    navController.navigate(
                        MangaScreens.MangaChapter.route
                                + "?server=$server"
                                + "&nameURL=$nameURL"
                                + "&chapterName=${prevChapter.name}"
                                + "&chapterURL=${prevChapter.url_name}"
                                + "&fromUserView=$fromUserView"
                    )
                },
                Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ChapterLabel(text = it.name)
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
        text = text,
        color = onBackground,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.widthIn(max = 150.dp),
        style = TextStyle(
            fontFamily = Dosis,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.75.sp
        )
    )
}
