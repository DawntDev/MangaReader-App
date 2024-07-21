package com.dawnt.mangareader.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dawnt.mangareader.schemas.NavScreens
import com.dawnt.mangareader.ui.theme.Background
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground

@Composable
fun NavBar(navController: NavController, modifier: Modifier) {
    val currentScreen = navController.currentBackStackEntry?.destination?.route
    Row(
        modifier = modifier
            .background(Background)
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavScreens.Screens.list.forEach {
            NavButton(
                screen = it,
                isSelected = currentScreen == it.route,
            ) {
                navController.navigate(it.route)
            }
        }
    }
}

@Composable
private fun NavButton(
    screen: NavScreens,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background =
        if (isSelected) Primary.copy(alpha = 0.1f)
        else Background

    val textColor =
        if (isSelected) Primary
        else onBackground

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = onClick, enabled = !isSelected)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "${screen.route}-icon",
                tint = textColor
            )

            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = screen.route.replaceFirstChar(Char::titlecase),
                    color = textColor,
                    style = TextStyle(
                        fontFamily = Dosis,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        lineHeight = 12.sp,
                        letterSpacing = 0.75.sp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun NavBarPreview() {
    NavBar(
        navController = NavController(LocalContext.current),
        modifier = Modifier
    )
}