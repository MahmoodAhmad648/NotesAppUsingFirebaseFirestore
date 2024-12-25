package com.mahmood.notesappusingfirebase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mahmood.notesappusingfirebase.navigation.Screens
import com.mahmood.notesappusingfirebase.ui.theme.ColorBlack
import com.mahmood.notesappusingfirebase.ui.theme.ColorWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier = Modifier,navController: NavHostController) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(3000)
        navController.navigate(Screens.NotesScreen.route){
            popUpTo(Screens.SplashScreen.route){
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = ColorBlack),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(3000)),
            exit = fadeOut(animationSpec = tween(3000))
        ) {
            Image(
                painter = painterResource(id = R.drawable.book), contentDescription = "logo",
                modifier = Modifier.size(100.dp)
            )



        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(3000)),
            exit = fadeOut(animationSpec = tween(3000))
        ){
            Text("Notes App", color = ColorWhite, fontSize = 20.sp)

        }

    }

}