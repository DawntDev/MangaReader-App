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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.DataStoreManager
import com.dawnt.mangareader.R
import com.dawnt.mangareader.schemas.NavScreens
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import kotlinx.coroutines.delay

@Composable
fun SplashController(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val anim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(2500),
        label = "Icon Anim"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        if (APIClient.isBuilt) {
            APIClient.getInstance().getMainScreen()
            APIClient.getInstance().getServers()
            delay(3000)
            navController.popBackStack()
            navController.navigate(NavScreens.HomeScreen.route)
        }
    }

    SplashComponent(
        alpha = anim.value,
        navigateToHome = {
            navController.popBackStack()
            navController.navigate(NavScreens.HomeScreen.route)
        }
    )
}

@Composable
private fun BaseURLField(
    navigateToHome: () -> Unit = {},
    alpha: Float = 1f
) {
    val focusManager = LocalFocusManager.current
    var key by remember { mutableIntStateOf(0) }
    var baseURL by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    LaunchedEffect(key) {
        if (key >= 1) {
            val currentConfig = DataStoreManager.currentConfig
            currentConfig.baseURL = baseURL

            DataStoreManager.saveConfigPreferences(currentConfig)

            if (APIClient.build()) {
                APIClient.getInstance().getMainScreen()
                APIClient.getInstance().getServers()
                navigateToHome()
            } else
                errorMsg = "Error URL not valid"
        }
    }

    TextField(
        value = baseURL,
        onValueChange = { baseURL = it },
        placeholder = {
            Text(
                text = "Server URL",
                textAlign = TextAlign.Left,
                style = TextStyle(
                    fontFamily = Dosis,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.75.sp
                ),
                modifier = Modifier.alpha(0.4f),
                color = Background
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                key++
            }
        ),
        modifier = Modifier
            .padding(top = 25.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .alpha(alpha)
            .background(onBackground)
    )
    Text(
        text = errorMsg,
        color = Primary,
        style = TextStyle(
            fontFamily = Dosis,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 12.sp,
            letterSpacing = 0.75.sp
        )
    )
}

@Composable
private fun SplashComponent(
    alpha: Float = 1f,
    navigateToHome: () -> Unit = {},
) {
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

            if (!APIClient.isBuilt) {
                BaseURLField(navigateToHome, alpha)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SplashPreview() {
    SplashComponent()
}