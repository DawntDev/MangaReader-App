package com.dawnt.mangareader.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dawnt.mangareader.R

// Set of Material typography styles to start with
val Dosis = FontFamily(
    Font(R.font.dosis_regular),
    Font(R.font.dosis_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.dosis_semibold, weight = FontWeight.SemiBold),
    Font(R.font.dosis_bold, weight = FontWeight.Bold),
    Font(R.font.dosis_medium, weight = FontWeight.Medium),
    Font(R.font.dosis_light, weight = FontWeight.Light),
    Font(R.font.dosis_extralight, weight = FontWeight.ExtraLight)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Dosis,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Dosis,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Dosis,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)