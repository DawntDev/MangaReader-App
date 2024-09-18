package com.dawnt.mangareader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dawnt.mangareader.DataStoreManager
import com.dawnt.mangareader.R
import com.dawnt.mangareader.schemas.MangaPreviewScheme
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground
import kotlinx.coroutines.delay

@Composable
fun InformationRow(
    rating: Float,
    typeOf: String,
    mangaPreview: MangaPreviewScheme
) {
    var startAnimation by remember { mutableStateOf(false) }
    var startMarkAnimation by remember { mutableStateOf(false) }
    var reverseMarkAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startMarkAnimation = DataStoreManager
            .getFavoriteMangas()
            .contains(mangaPreview)

        delay(300)
        reverseMarkAnimation = !startMarkAnimation
        startAnimation = true
    }

    // TODO: CHECK INTEGRATION
    LaunchedEffect(reverseMarkAnimation) {
        if (!reverseMarkAnimation) {
            if (!DataStoreManager.getFavoriteMangas().contains(mangaPreview)) {
                DataStoreManager.changeFavoriteState(mangaPreview)
                DataStoreManager.changeVisibility(mangaPreview.server, mangaPreview.name_url, true)
            }
        } else {
            if (DataStoreManager.getFavoriteMangas().contains(mangaPreview)) {
                DataStoreManager.changeFavoriteState(mangaPreview)
            }
        }
    }

    val starAnim by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.exploding_star
        )
    )

    val kanjiAnim by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.kanji_drawing
        )
    )

    val markAnim by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.bookmark_animation
        )
    )

    val progress by animateLottieCompositionAsState(
        composition = if (startAnimation) starAnim else null
    )

    val progressMark by animateLottieCompositionAsState(
        composition = if (startMarkAnimation) markAnim else null,
        speed = if (reverseMarkAnimation) -1.75f else 1f
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(secondaryBackground)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            LottieAnimation(
                composition = starAnim,
                progress = { progress },
                modifier = Modifier
                    .width(86.dp)
                    .height(86.dp)
                    .offset(y = (-8).dp)
            )
            LabelInfo(rating.toString(), Modifier.offset(y = (-28).dp))
        }
        Divider()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            LottieAnimation(
                composition = kanjiAnim,
                progress = { progress },
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
            )
            LabelInfo(typeOf.replaceFirstChar(Char::uppercaseChar), Modifier.offset(y = (-12).dp))
        }
        Divider()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    startMarkAnimation = true
                    reverseMarkAnimation = !reverseMarkAnimation
                }
        ) {
            LottieAnimation(
                composition = markAnim,
                progress = { progressMark },
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
            )
            LabelInfo("Favorite", Modifier.offset(y = (-12).dp))
        }
    }
}


@Composable
private fun Divider() {
    VerticalDivider(
        thickness = 1.dp,
        modifier = Modifier.height(56.dp),
        color = onBackground
    )
}

@Composable
private fun LabelInfo(value: String, modifier: Modifier) {
    Text(
        text = value,
        textAlign = TextAlign.Center,
        color = onBackground,
        modifier = modifier,
        overflow = TextOverflow.Visible,
        style = TextStyle(
            fontFamily = Dosis,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}


@Preview
@Composable
private fun InformationRowPreview() {
    InformationRow(
        rating = 4.5f,
        typeOf = "Manga",
        mangaPreview = MangaPreviewScheme(
            "Manga Title",
            "",
            1,
            ""
        )
    )
}