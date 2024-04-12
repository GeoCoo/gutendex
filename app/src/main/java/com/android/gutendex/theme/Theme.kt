package com.android.gutendex.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppTheme(
    darkTheme: Boolean = false /*isSystemInDarkTheme()*/,
    content: @Composable () -> Unit
) {
    val useDynamicColors = false//Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors = when {
        useDynamicColors && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        useDynamicColors && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        else -> themeColors(darkTheme = darkTheme).colors
    }

    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme
    val background = colors.background
    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = background,
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = background,
            darkIcons = useDarkIcons
        )

        onDispose {}
    }

    MaterialTheme(
        colorScheme = colors,
        typography = typography().typography,
        shapes = Shapes,
        content = content
    )
}