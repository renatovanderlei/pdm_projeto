package com.example.forestsurvey.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.forestsurvey.model.Parcela

@Composable
fun DetalhesParcelaScreen(
    navController: NavHostController,
    parcela: Parcela
) {
    // Lógica para carregar os detalhes da parcela com base no ID
    // Aqui você pode buscar a parcela no ViewModel ou no banco de dados

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Detalhes da Parcela", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "ID: $parcela", fontSize = 18.sp)
        // Exiba outros detalhes da parcela
    }
}