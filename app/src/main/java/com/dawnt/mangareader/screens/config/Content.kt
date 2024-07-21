package com.dawnt.mangareader.screens.config

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.dawnt.mangareader.DataStoreManager
import com.dawnt.mangareader.schemas.DownloadQuality
import com.dawnt.mangareader.schemas.Quality
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground

@Composable
fun ContentSection() {
    val currentConfig = DataStoreManager.currentConfig
    var nsfw by remember { mutableStateOf(currentConfig.nsfw) }
    var quality by remember { mutableStateOf(currentConfig.chapterRenderingQuality) }
    var downloadQuality by remember { mutableStateOf(currentConfig.downloadQuality) }

    LaunchedEffect(nsfw) {
        currentConfig.nsfw = nsfw
        DataStoreManager.saveConfigPreferences(currentConfig)
    }

    LaunchedEffect(quality) {
        currentConfig.chapterRenderingQuality = quality
        DataStoreManager.saveConfigPreferences(currentConfig)
    }

    LaunchedEffect(downloadQuality) {
        currentConfig.downloadQuality = downloadQuality
        DataStoreManager.saveConfigPreferences(currentConfig)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Option(text = "Allow NSFW content") {
            Switch(
                checked = nsfw,
                onCheckedChange = { nsfw = !nsfw },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Background,
                    checkedTrackColor = Primary,
                    checkedBorderColor = Primary,
                    uncheckedThumbColor = Background,
                    uncheckedTrackColor = secondaryBackground,
                    uncheckedBorderColor = secondaryBackground
                )
            )
        }

        Option(text = "Rendering Quality") {
            DropdownList(
                itemList = Quality.entries.map { it.name },
                selectedIndex = quality.ordinal,
                onItemClick = {
                    quality = Quality.entries.toTypedArray()[it]
                }
            )
        }

        Option(text = "Download Quality") {
            DropdownList(
                itemList = DownloadQuality.entries.map { it.name },
                selectedIndex = downloadQuality.ordinal
            ) {
                downloadQuality = DownloadQuality.entries.toTypedArray()[it]
            }
        }
    }
}

@Composable
private fun Option(text: String, callback: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = text,
            color = onBackground,
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp
            )
        )
        callback()
    }
    HorizontalDivider(
        thickness = 0.75.dp,
        color = secondaryBackground,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp)
    )
}

@Composable
fun DropdownList(
    itemList: List<String>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit
) {
    var showDropdown by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // button
        Box(
            modifier = Modifier.clickable { showDropdown = !showDropdown },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = itemList[selectedIndex],
                color = onBackground,
                style = TextStyle(
                    fontFamily = Dosis,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }

        Box {
            if (showDropdown) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(excludeFromSystemGesture = true),
                    onDismissRequest = { showDropdown = false }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .widthIn(max = 75.dp)
                            .background(secondaryBackground)
                            .padding(8.dp)
                            .verticalScroll(state = scrollState)
                    ) {
                        itemList.onEachIndexed { index, item ->
                            if (index != 0 && index != selectedIndex) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = Background,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            if (index != selectedIndex) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onItemClick(index)
                                            showDropdown = !showDropdown
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item,
                                        color = onBackground,
                                        style = TextStyle(
                                            fontFamily = Dosis,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                            lineHeight = 16.sp,
                                            letterSpacing = 0.5.sp

                                        )
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }

}