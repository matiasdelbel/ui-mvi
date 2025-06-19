package com.matiasdelbel.mvi.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val Pink40 = Color(0xFF7D5260)
private val Pink80 = Color(0xFFEFB8C8)
private val Purple40 = Color(0xFF6650a4)
private val Purple80 = Color(0xFFD0BCFF)
private val PurpleGrey40 = Color(0xFF625b71)
private val PurpleGrey80 = Color(0xFFCCC2DC)

val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
)
