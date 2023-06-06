package com.dawnt.mangareader.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.schemas.MangaPreview
import com.dawnt.mangareader.screens.MangaScreens
import com.dawnt.mangareader.utils.loadImage
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    elements: Array<MangaPreview>,
    APIConn: MangaReaderConnect,
    navController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 85.dp)
) {
    val pagerState = rememberPagerState(initialPage = 2)
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            pageCount = elements.size,
            state = pagerState,
            contentPadding = contentPadding,
            modifier = modifier.height(280.dp)
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .clickable {
                        APIConn.getMangaDetails(elements[it].nameURL)
                        navController.navigate(MangaScreens.MangaDetails.route)
                    }
                    .graphicsLayer {
                        val pageOffset =
                            (pagerState.currentPage - it) + pagerState.currentPageOffsetFraction
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                        )
                    }
            ) {
                val image = loadImage(URL = elements[it].coverURL)
                image.value?.let { img ->
                    Image(
                        bitmap = img.asImageBitmap(),
                        contentDescription = "Manga Image $it",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(180.dp)
                            .height(270.dp)
                    )
                }
            }
        }
    }
}
