package com.dawnt.mangareader.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.components.ChapterItem
import com.dawnt.mangareader.components.InformationRow
import com.dawnt.mangareader.components.Loader
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.schemas.MangaPreview
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground
import com.dawnt.mangareader.utils.DataStorage
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.utils.loadImage

@Composable
fun MangaDetail(navController: NavController, APIConn: MangaReaderConnect) {
    val viewModel: MangaReaderConnect = APIConn
    val data by viewModel.mangaDetail.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        data?.let {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, start = 8.dp, end = 8.dp)
            ) {
                val image = loadImage(URL = it.coverURL)
                image.value?.let { img ->
                    Image(
                        bitmap = img.asImageBitmap(),
                        contentDescription = it.nameURL,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .width(165.dp)
                            .height(245.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Text(
                    text = it.title,
                    color = onBackground,
                    style = TextStyle(
                        fontFamily = Dosis,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 240.dp)
                )
                InformationRow(
                    rating = it.rating,
                    typeOf = it.typeOf,
                    mangaPreview = MangaPreview(it.title, it.nameURL, it.coverURL)
                )
                LazyColumn {
                    item { LabelText(text = "Description") }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 20.dp, max = 240.dp)
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(secondaryBackground)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = it.overview,
                                color = onBackground,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(all = 12.dp),
                                style = TextStyle(
                                    fontFamily = Dosis,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    letterSpacing = 0.25.sp
                                )
                            )
                        }
                    }
                    item { LabelText(text = "Chapters") }
                    item {
                        val manga = DataStorage.currentChaptersViewed[it.nameURL]
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 240.dp, min = 20.dp)
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(secondaryBackground)
                        ) {
                            items(it.chapterList) { chapter ->
                                ChapterItem(
                                    chapter,
                                    it.nameURL,
                                    manga?.chapters?.contains(chapter) ?: false,
                                    navController,
                                    APIConn
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(56.dp)) }
                }
            }
        } ?: run { Loader(modifier = Modifier.align(Alignment.Center)) }
        NavBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LabelText(text: String) {
    Text(
        text = text,
        color = onBackground,
        textAlign = TextAlign.Start,
        style = TextStyle(
            fontFamily = Dosis,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 12.dp)
    )
}
