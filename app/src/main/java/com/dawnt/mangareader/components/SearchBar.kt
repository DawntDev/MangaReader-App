package com.dawnt.mangareader.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.R
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground

@Composable
fun SearchBar(
    genres: Map<String, Color>,
    modifier: Modifier
) {
    var text by remember { mutableStateOf("") }
    var selectGenres by remember { mutableStateOf<List<String>>(listOf()) }
    var isOpen by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(text, selectGenres) {
        APIClient.getInstance().getMangaSearch(
            text,
            selectGenres.toTypedArray()
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(Background)
                .padding(top = 38.dp, start = 6.dp, end = 6.dp, bottom = 12.dp)
                .fillMaxWidth()
                .height(48.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = TextFieldDefaults.colors(
                    cursorColor = onBackground,
                    focusedIndicatorColor = Primary,
                    unfocusedIndicatorColor = Color.Unspecified,
                    focusedContainerColor = secondaryBackground,
                    unfocusedContainerColor = secondaryBackground,
                    disabledContainerColor = secondaryBackground,
                    focusedTextColor = onBackground,
                    unfocusedTextColor = onBackground,
                    disabledTextColor = onBackground,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 5.dp)
                            .size(26.dp),
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        tint = onBackground,
                        contentDescription = "Search Icon"
                    )
                },
                placeholder = {
                    Text(
                        text = "Search Manga",
                        textAlign = TextAlign.Left,
                        style = TextStyle(),
                        modifier = Modifier.alpha(0.4f),
                        color = onBackground
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            text = ""
                            focusManager.clearFocus()
                        }
                    ) {
                        if (text.isNotBlank()) {
                            Icon(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(16.dp),
                                painter = painterResource(id = R.drawable.outline_cancel_24),
                                tint = onBackground,
                                contentDescription = "Cancel Icon"
                            )
                        }
                    }
                }
            )
            IconButton(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isOpen) secondaryBackground.copy(0.7f)
                        else secondaryBackground
                    ),
                onClick = { isOpen = !isOpen }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_menu_24),
                    contentDescription = "Menu Icon",
                    tint = onBackground
                )
            }
        }
        AnimatedVisibility(visible = isOpen) {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Background)
            ) {
                items(genres.toList().chunked(3)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        for ((genre, value) in it) {
                            var isActive by remember { mutableStateOf(selectGenres.contains(genre)) }
                            GenreIcon(
                                genre = genre,
                                color = if (isActive) value else onBackground,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                            ) {
                                isActive = !isActive
                                selectGenres = if (isActive) {
                                    selectGenres + genre
                                } else {
                                    selectGenres - genre
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    SearchBar(
        genres = mapOf(
            "genre1" to Color.Red,
            "genre2" to Color.Green,
            "genre3" to Color.Blue
            ),
        modifier = Modifier
    )
}