package com.example.forestsurvey.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.forestsurvey.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(1500)
        navController.navigate("login")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF003300)), // Define o fundo verde escuro
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_fp), // Use painterResource para carregar a imagem
            contentDescription = "Imagem de Splash"
        )
    }
}
