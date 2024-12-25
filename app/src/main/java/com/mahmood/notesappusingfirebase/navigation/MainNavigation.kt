package com.mahmood.notesappusingfirebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mahmood.notesappusingfirebase.InsertScreen
import com.mahmood.notesappusingfirebase.NotesScreen
import com.mahmood.notesappusingfirebase.SplashScreen

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(Screens.SplashScreen.route) {
            SplashScreen(
                modifier,
                navController = navController
            )
        }

        composable(Screens.NotesScreen.route) {
            NotesScreen(
                modifier,
                navController = navController
            )
        }
        composable(Screens.InsertScreen.route + "/{id}") {
            val id = it.arguments?.getString("id")
            InsertScreen(modifier,navController,id)
        }

    }


}