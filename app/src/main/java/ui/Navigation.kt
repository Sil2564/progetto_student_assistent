package com.silvianikikarim.studentassistant.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel

@Composable
fun AppNavigation(votoViewModel: VotoViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.ORARIO) {
            OrarioScreen()
        }

        composable(Routes.APPUNTI) {
            AppuntiScreen()
        }

        composable(Routes.CALENDARIO_STUDIO) {
            CalendarioStudioScreen()
        }

        composable(Routes.ANDAMENTO) {
            AndamentoScreen(votoViewModel)
        }

        composable(Routes.CONSIGLI) {
            ConsigliScreen()
        }

        composable(Routes.IMPOSTAZIONI) {
            ImpostazioniScreen()
        }
    }
}
