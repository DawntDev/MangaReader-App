package com.dawnt.mangareader.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.schemas.MangaPreviewScheme
import com.dawnt.mangareader.schemas.MangaScreens
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground

@Composable
fun GridImages(
    items: Array<MangaPreviewScheme>,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        for (row in items.toList().chunked(3)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (item in row) {
                    GridItem(
                        title = item.title,
                        server = item.server,
                        nameURL = item.name_url,
                        coverURL = item.cover_url,
                        navController = navController
                    )
                }
            }
        }
    }
}


@Composable
private fun GridItem(
    title: String,
    server: Int,
    nameURL: String,
    coverURL: String?,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            APIClient.getInstance().getMangaDetails(server, nameURL)
            navController.navigate(MangaScreens.MangaDetails.route)
        }
    ) {
        CoilImage(
            url = coverURL,
            modifier = Modifier
                .width(120.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(
            text = title,
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = onBackground,
            modifier = Modifier.widthIn(max = 100.dp)
        )
    }
}


@Preview
@Composable
private fun GridImagesPreview() {
    GridImages(
        items = arrayOf(
            MangaPreviewScheme("Manga Title", "", 1, ""),
            MangaPreviewScheme("Manga Title", "", 1, ""),
            MangaPreviewScheme("Manga Title", "", 1, ""),
            MangaPreviewScheme("Manga Title", "", 1, ""),
            MangaPreviewScheme("Manga Title", "", 1, ""),
            MangaPreviewScheme("Manga Title", "", 1, ""),
            MangaPreviewScheme("Manga Title", "", 1, ""),
            MangaPreviewScheme("Manga Title", "", 1, ""),
            MangaPreviewScheme("Manga Title", "", 1, ""),
        ),
        navController = rememberNavController()
    )
}
