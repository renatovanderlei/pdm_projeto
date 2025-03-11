package com.example.forestsurvey.navigation

import ColetaScreen
import LoginScreen
import MainScreen
import PreenchimentoDadosScreen
import RegisterScreen
import com.example.forestsurvey.ui.screens.MinhasParcelasScreen
import SubplotScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forestsurvey.model.MainViewModel
import com.example.forestsurvey.fb.FBDatabase
import com.example.forestsurvey.model.MainViewModelFactory
import com.example.forestsurvey.ui.screens.SplashScreen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import com.example.forestsurvey.ui.screens.DetalhesParcelaScreen

@Composable
fun AppNavigator(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Cria uma instância do ViewModel
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(FBDatabase()))

    // Obtém o usuário logado e o estado das parcelas
    val user = viewModel.user.value
    val parcelas = viewModel.parcelas

    NavHost(navController = navController, startDestination = "splash", modifier = modifier) {
        // Splash Screen
        composable("splash") { SplashScreen(navController) }

        // Telas de Autenticação
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }

        // Telas Principais
        composable("main") { MainScreen(navController) }

        // Tela Minhas Parcelas
        composable("minhasParcelas") {
            if (user != null && parcelas.value != null) {
                MinhasParcelasScreen(
                    navController = navController,
                    parcelasState = parcelas,
                    usuarioLogado = user
                )
            } else {
                CircularProgressIndicator() // Indicador de carregamento
            }
        }

        // Tela Coleta
        composable("coleta") {
            if (user != null) {
                ColetaScreen(
                    navController = navController,
                    usuarioLogado = user,
                    onAdicionarParcela = { novaParcela, userId ->
                        if (userId.isBlank()) {
                            println("Erro: userId não pode ser vazio")
                        } else {
                            viewModel.onAdicionarParcela(novaParcela, userId)
                        }
                    }
                )
            } else {
                CircularProgressIndicator()
            }
        }

        // Tela Detalhes da Parcela
        composable(
            route = "detalhes_parcela/{parcelaId}",
            arguments = listOf(navArgument("parcelaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val parcelaId = backStackEntry.arguments?.getString("parcelaId")
                ?: throw IllegalArgumentException("ParcelaId não pode ser nulo")
            val parcela = viewModel.parcelas.value?.find { it.id == parcelaId }
            if (parcela != null) {
                DetalhesParcelaScreen(navController, parcela) // Passa o objeto Parcela
            } else {
                Text(text = "Parcela não encontrada")
            }
        }

        // Tela Subplot
        composable(
            route = "subplots/{parcelaNome}/{ruaId}",
            arguments = listOf(
                navArgument("parcelaNome") { type = NavType.StringType },
                navArgument("ruaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val parcelaNome = backStackEntry.arguments?.getString("parcelaNome")
                ?: throw IllegalArgumentException("ParcelaNome não pode ser nulo")
            val ruaId = backStackEntry.arguments?.getString("ruaId")?.toIntOrNull()
                ?: throw IllegalArgumentException("RuaId inválido")
            SubplotScreen(navController, parcelaNome, ruaId)
        }

        // Tela Preenchimento Dados
        composable(
            route = "preenchimento_dados/{parcelaNome}/{ruaId}/{subplotId}",
            arguments = listOf(
                navArgument("parcelaNome") { type = NavType.StringType },
                navArgument("ruaId") { type = NavType.StringType },
                navArgument("subplotId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val parcelaNome = backStackEntry.arguments?.getString("parcelaNome")
                ?: throw IllegalArgumentException("ParcelaNome não pode ser nulo")
            val ruaId = backStackEntry.arguments?.getString("ruaId")?.toIntOrNull()
                ?: throw IllegalArgumentException("RuaId inválido")
            val subplotId = backStackEntry.arguments?.getString("subplotId")
                ?: throw IllegalArgumentException("SubplotId não pode ser nulo")
            PreenchimentoDadosScreen(parcelaNome, ruaId, subplotId, navController)
        }
    }
}