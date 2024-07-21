package com.dawnt.mangareader.screens.config

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dawnt.mangareader.APIClient
import com.dawnt.mangareader.DataStoreManager
import com.dawnt.mangareader.ui.theme.Dosis
import com.dawnt.mangareader.ui.theme.Primary
import com.dawnt.mangareader.ui.theme.onBackground
import com.dawnt.mangareader.ui.theme.secondaryBackground
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Composable
fun MemorySection() {
    val context = LocalContext.current
    var keyClearData by remember { mutableIntStateOf(0) }

    LaunchedEffect(keyClearData) {
        if (keyClearData >= 1) {
            val genresFile = File(context.filesDir, "genres.json")
            val downloadFolder = File(context.filesDir, "downloads")

            DataStoreManager.clearPreferences()
            if (genresFile.exists()) genresFile.delete()
            if (downloadFolder.exists()) downloadFolder.delete()
        }
    }

    ButtonSection(
        text = "Clear Data",
        warning = "By deleting the data you will delete all the information necessary " +
                "for the application to work correctly. This should not generate any " +
                "problem, it would be as if you were reinstalling the application from " +
                "scratch. When you confirm this action, the following elements will be deleted:" +
                "\n\n\t> Config Preferences\n\t> Genres List\n\t> Chapters Viewed\n\t> Favorite Mangas List" +
                "\n\t> Downloaded Chapters" + "\n\n THIS ACTION IS IRREVERSIBLE."
    ) { keyClearData++ }

    ButtonSection(
        text = "Fetch Genres",
        warning = "If you wish, you can update the list of genres, given by the server, in order to maintain a good reach when searching by genre. Remember that the server updates this list every 5 days, so it is not necessary to be constantly updating it.\n" +
                "\n" +
                "In case the server cannot give an adequate response, the current list will be maintained."
    ) {
        val genres = APIClient.getInstance().getGenres()
        if (genres != null) {
            context.openFileOutput("genres.json", Context.MODE_PRIVATE).use {
                it.write(Json.encodeToString(genres).toByteArray())
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun ButtonSection(text: String, warning: String, onClick: () -> Unit = {}) {
    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (visible) Primary else secondaryBackground,
            contentColor = onBackground
        ),
        shape = RectangleShape,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .height(48.dp),
        onClick = { visible = !visible }
    ) {
        Text(text = text)
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically {
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .clip(RectangleShape)
                .background(secondaryBackground)
                .padding(12.dp)
        ) {
            Text(
                text = warning,
                modifier = Modifier.padding(bottom = 18.dp),
                color = onBackground,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Justify,
                    fontFamily = Dosis,
                    fontWeight = FontWeight.Normal,

                    )
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = onBackground
                ),
                onClick = {
                    onClick()
                    visible = false
                }
            ) {
                Text(
                    text = "CONFIRM",
                    color = onBackground,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Justify,
                        fontFamily = Dosis,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }
    }
}