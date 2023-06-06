package com.dawnt.mangareader.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ChapterItem(
    chapter: String,
    nameURL: String,
    viewed: Boolean,
    navController: NavController,
    APIConn: MangaReaderConnect
) {
    val download = SwipeAction(
        onSwipe = { /* TODO: DOWNLOAD */ },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.outline_download_24),
                contentDescription = "Download Icon",
                tint = onBackground
            )
        },
        background = Color(0xFF52b69A),
    )

    SwipeableActionsBox(endActions = listOf(download), swipeThreshold = 72.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable {
                    APIConn.getMangaChapter(nameURL, chapter)
                    navController.navigate(
                        MangaScreens.MangaChapter.route
                                + "?nameURL=$nameURL"
                                + "&chapter=$chapter"
                    )
                }
        ) {
            Text(
                text = "Chapter " + chapter.replace(regex = Regex("[_\\-]"), "."),
                color = if (!viewed) onBackground else onBackground.copy(alpha = 0.3f),
                style = TextStyle(
                    fontFamily = Dosis,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.25.sp
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}
