package com.dawnt.mangareader.screens.config

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dawnt.mangareader.R
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground

@Composable
fun CreditsSection() {
    Column {
        Text(
            text = "Development by @DawntDev",
            modifier = Modifier.padding(bottom = 12.dp),
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                color = onBackground,
                textAlign = TextAlign.Center
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(secondaryBackground)
        ) {
            SocialMediaButton(
                text = "GitHub",
                icon = R.drawable.github_icon,
                url = "https://github.com/DawntDev",
                tint = Color.White
            )
            SocialMediaButton(
                text = "Discord",
                icon = R.drawable.discord_icon,
                url = "https://discord.gg/mexicodev",
                tint = Color(0xFF5865F2)
            )
            SocialMediaButton(
                text = "CodeWars",
                icon = R.drawable.codewars_icon,
                url = "https://www.codewars.com/users/Dawnt",
                tint = Color(0xFFA33F29)
            )
        }
        Text(
            text = "© 2023 MangaReader - All Rights Reserved",
            modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
            style = TextStyle(
                fontFamily = Dosis,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                color = onBackground,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp
            )
        )
    }
}

@Composable
private fun SocialMediaButton(text: String, icon: Int, url: String, tint: Color) {
    val context = LocalContext.current
    IconButton(
        modifier = Modifier.size(48.dp),
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = tint,
            contentDescription = "Social Media Button $text",
            modifier = Modifier.fillMaxSize()
        )
    }
}