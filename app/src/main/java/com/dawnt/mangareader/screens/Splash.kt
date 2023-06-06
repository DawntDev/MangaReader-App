package com.dawnt.mangareader.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.dawnt.mangareader.R
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import kotlinx.coroutines.delay

@Composable
fun SplashController(navController: NavController, APIConn: MangaReaderConnect) {
    var startAnimation by remember { mutableStateOf(false) }
    val anim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(2500)
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        APIConn.getMainScreen()
        delay(3000)
        navController.popBackStack()
        navController.navigate(NavScreens.HomeScreen.route)
    }

    SplashComponent(alpha = anim.value)
}

@Composable
private fun SplashComponent(alpha: Float = 1f) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .alpha(alpha = alpha)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_adaptive_fore),
                contentDescription = "MangaReader Icon",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 15.dp)
            )
            Text(
                text = "MangaReader",
                color = Color.White,
                style = TextStyle(
                    fontFamily = Dosis,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}
