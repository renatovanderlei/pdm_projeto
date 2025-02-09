package com.example.forestsurvey.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp
import com.example.forestsurvey.R

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF003300), // Verde escuro
    secondary = Color(0xFF004D40),
    tertiary = Color(0xFF004D40),
    background = Color(0xFF003300), // Fundo verde escuro
    surface = Color(0xFF003300),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF003300), // Verde escuro
    secondary = Color(0xFF004D40),
    tertiary = Color(0xFF004D40),
    background = Color(0xFF003300), // Fundo verde escuro
    surface = Color(0xFF003300),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

val Calibri = FontFamily(
    Font(R.font.calibri_regular, FontWeight.Normal),
    Font(R.font.calibri_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Calibri,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Calibri,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
)

@Composable
fun ForestSurveyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
