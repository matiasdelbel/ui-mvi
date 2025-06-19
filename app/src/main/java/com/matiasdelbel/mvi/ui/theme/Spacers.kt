package com.matiasdelbel.mvi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacers(
    val none: Dp,
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp,
)

val MaterialTheme.spacers: Spacers
    @Composable get() = LocalSpacers.current

val LocalSpacers = staticCompositionLocalOf { DefaultSpacers }

val DefaultSpacers = Spacers(
    none = 0.dp,
    xs = 4.dp,
    sm = 8.dp,
    md = 16.dp,
    lg = 32.dp,
    xl = 48.dp,
)
