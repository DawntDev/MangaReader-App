package com.dawnt.mangareader.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.schemas.MangaPreview
import com.dawnt.mangareader.screens.MangaScreens
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.utils.loadImage

@Composable
fun GridImages(
    items: Array<MangaPreview>,
    navController: NavController,
    APIConn: MangaReaderConnect
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
                        nameURL = item.nameURL,
                        coverURL = item.coverURL,
                        navController = navController,
                        APIConn = APIConn
                    )
                }
            }
        }
    }
}


@Composable
private fun GridItem(
    title: String,
    nameURL: String,
    coverURL: String?,
    navController: NavController,
    APIConn: MangaReaderConnect
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            APIConn.getMangaDetails(nameURL)
            navController.navigate(MangaScreens.MangaDetails.route)
        }
    ) {
        val image = loadImage(URL = coverURL)
        image.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
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
