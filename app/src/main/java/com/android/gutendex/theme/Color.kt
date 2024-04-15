package com.android.gutendex.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class ThemeColorsTemplate(
    private val darkTheme: Boolean, val light: ColorScheme, val dark: ColorScheme
) {
    val colors: ColorScheme
        get() {
            return when (darkTheme) {
                true -> dark
                false -> light
            }
        }
}


val md_theme_light_primary = Color(0xFFAE907B)
val md_theme_light_primaryContainer = Color(0xfff6f0e5)
val md_theme_light_secondaryContainer = Color(0xFFE9DDC7)
val md_theme_light_onSurface = Color(0xffFDFAF4)
val md_theme_light_surfaceTint = Color(0xFF000000)
val md_theme_light_outlineVariant = Color(0x80404040)
val md_theme_light_scrim = Color(0xFFFFFFFF)



private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    primaryContainer = md_theme_light_primaryContainer,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSurface = md_theme_light_onSurface,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val DarkColors = darkColorScheme()

@Composable
fun themeColors(darkTheme: Boolean): ThemeColorsTemplate {
    return ThemeColorsTemplate(darkTheme, LightColors, DarkColors)
}
