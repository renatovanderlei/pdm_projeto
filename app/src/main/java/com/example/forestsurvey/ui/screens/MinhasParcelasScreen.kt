package com.example.forestsurvey.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.forestsurvey.model.Parcela
import com.example.forestsurvey.model.User
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment

@Composable
fun MinhasParcelasScreen(
    navController: NavHostController,
    parcelasState: State<List<Parcela>>,
    usuarioLogado: User
) {
    val parcelasCriadas = parcelasState.value.filter { it.userId == usuarioLogado.email }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Minhas Parcelas",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (parcelasCriadas.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(parcelasCriadas) { parcela ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("detalhes_parcela/${parcela.id}")
                                }
                                .padding(16.dp)
                        ) {
                            Text(text = "Parcela: ${parcela.id}", fontSize = 18.sp)
                            Text(text = "Nome: ${parcela.nome}", fontSize = 16.sp)
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma parcela criada.",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}