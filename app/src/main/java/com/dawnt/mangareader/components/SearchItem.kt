package com.dawnt.mangareader.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.utils.loadImage

@Composable
fun SearchItem(
    navController: NavController,
    APIConn: MangaReaderConnect,
    title: String,
    typeOf: String,
    rating: Float,
    genres: Map<String, Color>,
    nameURL: String,
    coverURL: String?
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .height(178.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(secondaryBackground)
            .clickable {
                APIConn.getMangaDetails(nameURL)
                navController.navigate(MangaScreens.MangaDetails.route)
            }
    ) {
        val image = loadImage(URL = coverURL)
        image.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Cover Manga",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 6.dp)
                    .width(112.dp)
                    .height(165.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(vertical = 6.dp)
                .fillMaxSize()
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Dosis,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = onBackground,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    contentDescription = "Rating",
                    tint = Color(0xFFF7D94B),
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(24.dp)
                )
                Text(
                    text = "$rating",
                    color = onBackground,
                    modifier = Modifier.padding(end = 6.dp),
                    style = TextStyle(
                        fontFamily = Dosis,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 0.5.sp,
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_kanji_24),
                    contentDescription = "Rating",
                    tint = Primary,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = typeOf,
                    color = onBackground,
                    style = TextStyle(
                        fontFamily = Dosis,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 0.5.sp,
                    )
                )
            }
            LazyRow(modifier = Modifier.fillMaxWidth(0.95f)) {
                genres.forEach { (genre, color) ->
                    item {
                        GenreIcon(
                            genre = genre,
                            color = color,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                }
            }
        }
    }
}