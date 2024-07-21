package com.dawnt.mangareader.screens.manga

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.DataStoreManager
import com.dawnt.mangareader.R
import com.dawnt.mangareader.components.ChapterNavBar
import com.dawnt.mangareader.components.CoilImage
import com.dawnt.mangareader.components.Loader
import com.dawnt.mangareader.schemas.ChapterScheme
import com.dawnt.mangareader.schemas.MangaChapterViewedPreferences
import com.dawnt.mangareader.schemas.MangaScreens
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground


private val REGEX_IMG = Regex(
    pattern = "(http(s?):)([/|.\\w\\s-])*\\.(?:jpg|gif|png|webp|jpeg)"
)
private val REGEX_B64 = Regex(
    pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$",
    option = RegexOption.MULTILINE
)

private fun isEncodeB64(string: String): Boolean {
    return REGEX_B64.matches(string)
}

private fun isImageURL(string: String): Boolean {
    return REGEX_IMG.matches(string)
}

@Composable
fun MangaChapter(
    server: Int,
    nameURL: String,
    chapter: ChapterScheme,
    navController: NavController,
    fromUserView: Boolean = false
) {
    val viewModel: APIClient = APIClient.getInstance()
    val data by viewModel.mangaChapter.observeAsState()
    val manga by viewModel.mangaDetail.observeAsState()
    val error by viewModel.requestError.observeAsState()

    val scrollState = rememberLazyListState()
    val isClicked = remember { mutableStateOf(false) }
    val isHeaderVisible = remember(scrollState) {
        derivedStateOf {
            isClicked.value = false
            scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset == 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(top = 28.dp)
    ) {
        data?.let {
            AnimatedVisibility(
                visible = isHeaderVisible.value xor isClicked.value,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Row( // Header
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(secondaryBackground)
                        .align(Alignment.TopCenter)
                ) {
                    IconButton(
                        modifier = Modifier.fillMaxHeight(),
                        onClick = {
                            if (!fromUserView) navController.popBackStack() else {
                                navController.navigate(MangaScreens.MangaDetails.route)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_list_24),
                            contentDescription = "Return",
                            tint = onBackground,
                            modifier = Modifier
                                .size(28.dp)
                                .scale(-1f)
                        )
                    }
                    Text(
                        text = chapter.name,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 6.dp),
                        color = onBackground,
                        style = TextStyle(
                            fontFamily = Dosis,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp
                        )
                    )
                }
            }
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .zIndex(-1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isClicked.value = !isClicked.value }
            ) {
                // Content
                items(it) {
                    when {
                        isImageURL(it) -> {
                            CoilImage(
                                url = it,
                                contentDescription = "ImageChapter",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxSize(),
                                quality = DataStoreManager.currentConfig.chapterRenderingQuality.filterQuality
                            )

                        }

                        isEncodeB64(it) -> { /* TODO*/ }

                        else -> {
                            Text(
                                text = it,
                                color = onBackground,
                                textAlign = TextAlign.Justify,
                                style = TextStyle(
                                    fontFamily = Dosis,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    lineHeight = 16.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                item {
                    var chaptersViewed by remember {
                        mutableStateOf<MangaChapterViewedPreferences?>(null)
                    }

                    LaunchedEffect(Unit) {
                        chaptersViewed = DataStoreManager.getChaptersViewed(
                            server = server,
                            nameURL = nameURL
                        )

                        chaptersViewed?.let {
                            if (it.chapters.contains(chapter))
                                it.chapters.remove(chapter)

                            it.chapters.add(0, chapter)
                            DataStoreManager.addChaptersViewed(
                                server = server,
                                nameURL = nameURL,
                                value = it
                            )

                        } ?: run {
                            DataStoreManager.addChaptersViewed(
                                server = server,
                                nameURL = nameURL,
                                value = MangaChapterViewedPreferences(
                                    manga?.title!!,
                                    manga?.cover_url!!,
                                    mutableListOf(chapter)
                                )
                            )
                        }
                    }

                    ChapterNavBar(
                        navController = navController,
                        server = server,
                        nameURL = nameURL,
                        currentChapter = chapter,
                        chapters = manga?.chapters_list!!,
                        fromUserView = fromUserView
                    )
                }
            }
        } ?: run {
            if (error != null) {
                Text(
                    text = error!!,
                    color = Primary,
                    style = TextStyle(
                        fontFamily = Dosis,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Justify
                    ),
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 40.dp)
                        .align(Alignment.Center)
                )
            } else {
                Loader(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}