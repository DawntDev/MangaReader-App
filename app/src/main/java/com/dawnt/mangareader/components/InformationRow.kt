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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dawnt.mangareader.R
import com.dawnt.mangareader.schemas.MangaPreview
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground
import com.dawnt.mangareader.utils.DataStorage
import kotlinx.coroutines.delay

@Composable
fun InformationRow(
    rating: Float,
    typeOf: String,
    mangaPreview: MangaPreview
) {
    var startAnimation by remember { mutableStateOf(false) }
    var startMarkAnimation by remember {
        mutableStateOf(
            DataStorage.currentFavoriteMangas.contains(
                mangaPreview
            )
        )
    }
    var reverseMarkAnimation by remember { mutableStateOf(!startMarkAnimation) }

    LaunchedEffect(reverseMarkAnimation) {
        if (!reverseMarkAnimation) {
            if (!DataStorage.currentFavoriteMangas.contains(mangaPreview)) {
                DataStorage.currentFavoriteMangas.add(0, mangaPreview)
                DataStorage.saveFavoriteMangas()
                DataStorage.saveVisibility(mangaPreview.nameURL, true)
            }
        } else {
            if (DataStorage.currentFavoriteMangas.contains(mangaPreview)) {
                DataStorage.currentFavoriteMangas.remove(mangaPreview)
                DataStorage.saveFavoriteMangas()
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(300)
        startAnimation = true
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
        VerticalDivider()
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
            LabelInfo(typeOf, Modifier.offset(y = (-12).dp))
        }
        VerticalDivider()
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
private fun VerticalDivider() {
    Divider(
        color = onBackground,
        thickness = 1.dp,
        modifier = Modifier
            .height(56.dp)
            .width(1.dp)
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
