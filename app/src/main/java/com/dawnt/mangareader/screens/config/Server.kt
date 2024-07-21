package com.dawnt.mangareader.screens.config

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.DataStoreManager
import com.dawnt.mangareader.R
import com.dawnt.mangareader.components.Loader
import com.dawnt.mangareader.schemas.ServerScheme
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground


@Composable
fun ServerSection(viewModel: APIClient) {
    var key by remember { mutableIntStateOf(0) }
    val currentServers by viewModel.servers.observeAsState()
    val currentConfig = DataStoreManager.currentConfig
    var baseURLEdit by remember { mutableStateOf(currentConfig.baseURL) }
    var changedServer by remember { mutableStateOf(baseURLEdit != currentConfig.baseURL) }

    LaunchedEffect(key) {
        if (key >= 1) {
            currentConfig.baseURL = baseURLEdit
            DataStoreManager.saveConfigPreferences(currentConfig)
            if (APIClient.build()) {
                APIClient.getInstance().getMainScreen()
                changedServer = false
            }
        } else {
            if (APIClient.build()) {
                APIClient.getInstance().getServers()
            }
        }
    }

    OutlinedTextField(
        value = baseURLEdit,
        onValueChange = {
            baseURLEdit = it
            changedServer = baseURLEdit != currentConfig.baseURL
        },
        label = { Text("Server URL") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = TextFieldDefaults.colors(
            cursorColor = onBackground,
            focusedIndicatorColor = Primary,
            unfocusedIndicatorColor = secondaryBackground,
            focusedContainerColor = Background,
            unfocusedContainerColor = Background,
            disabledContainerColor = Background,
            focusedTextColor = onBackground,
            unfocusedTextColor = onBackground,
            disabledTextColor = onBackground,
            focusedLabelColor = Primary
        ),
        trailingIcon = {
            AnimatedVisibility(
                visible = changedServer,
                modifier = Modifier.zIndex(1f)
            ) {
                IconButton(onClick = { key++ }) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.baseline_check_24
                        ),
                        contentDescription = "Check Icon",
                        tint = Primary
                    )
                }
            }
        }
    )
    currentServers?.let { servers ->
        servers.forEach {
            ServerItem(server = it)
        }
    } ?: run {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Loader()
        }
    }
}

@Composable
private fun ServerItem(server: ServerScheme) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .padding(vertical = 12.dp, horizontal = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(secondaryBackground)
    ) {
        Text(
            text = server.name,
            color = onBackground,
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(start = 16.dp)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${server.elements}", color = Primary, style = TextStyle(
                    fontFamily = Dosis,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = "Elements", color = onBackground, style = TextStyle(
                    fontFamily = Dosis,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Text(
            text = "NSFW",
            color = if (server.nsfw) Primary else Background,
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            )
        )
        IconButton(onClick = { /*TODO*/ }) {
            if (server.in_working) {
                Loader(circleSize = 8.dp, spaceBetween = 4.dp, travelDistance = 10.dp)
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_refresh_24),
                    contentDescription = "Check Icon",
                    tint = Primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}
