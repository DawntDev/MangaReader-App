package com.dawnt.mangareader.screens.config

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.components.NavBar
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Config(navController: NavController) {
    val viewModel: APIClient = APIClient.getInstance()
    val currentServers by viewModel.servers.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp, start = 8.dp, end = 8.dp)
        ) {
            stickyHeader { Section(name = "Server") }
            item { ServerSection(currentServers) }
            stickyHeader { Section(name = "Content") }
            item { ContentSection() }
            stickyHeader { Section(name = "Memory") }
            item { MemorySection() }
            item { Section(name = "Credits") }
            item { CreditsSection() }
            item {Spacer(modifier = Modifier.height(64.dp))}
        }
        NavBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
private fun Section(name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = name,
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 24.sp,
                color = Primary,
                textAlign = TextAlign.Center
            ),
        )
        HorizontalDivider(
            thickness = 4.dp,
            color = Primary,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun ConfigPreview() {
    Config(navController = rememberNavController())
}