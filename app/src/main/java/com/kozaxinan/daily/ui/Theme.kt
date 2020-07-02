package com.kozaxinan.daily.ui

import androidx.compose.Composable
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.graphics.Color
import androidx.ui.material.MaterialTheme
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette

private val DarkColorPalette = darkColorPalette(
  primary = purple200,
  primaryVariant = purple700,
  secondary = teal200
)

private val LightColorPalette = lightColorPalette(
  primary = purple500,
  primaryVariant = purple700,
  secondary = teal200,
  background = Color.LightGray,
  surface = Color.White,
  onPrimary = Color.DarkGray,
  onSecondary = Color.DarkGray,
  onBackground = Color.Black,
  onSurface = Color.DarkGray
)

@Composable
fun DailyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  MaterialTheme(
    colors = colors,
    typography = typography,
    shapes = shapes,
    content = content
  )
}
