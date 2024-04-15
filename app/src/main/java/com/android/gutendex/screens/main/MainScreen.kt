package com.android.gutendex.screens.main

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.gutendex.helpers.LifecycleEffect
import com.android.gutendex.models.NavRoutes
import com.android.gutendex.screens.books.BooksScreen
import com.android.gutendex.screens.info.InfoScreen


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.BOOKS.route
        ) {
            composable(route = NavRoutes.BOOKS.route) {
                BooksScreen(navController)
            }
            composable(
                route = NavRoutes.BOOK.route,
                arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                InfoScreen(navController, backStackEntry.arguments?.getString("id") ?: "")
            }
        }
    }
}




