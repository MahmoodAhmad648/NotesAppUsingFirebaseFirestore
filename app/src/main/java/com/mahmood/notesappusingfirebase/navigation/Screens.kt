package com.mahmood.notesappusingfirebase.navigation

sealed class Screens(val route: String) {

    object SplashScreen : Screens("splash_screen")
    object NotesScreen : Screens("notes_screen")
    object InsertScreen : Screens("insert_screen")






}