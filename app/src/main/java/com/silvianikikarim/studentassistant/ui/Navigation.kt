package com.silvianikikarim.studentassistant.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel
import com.silvianikikarim.studentassistant.viewmodel.SettingsViewModel
import com.silvianikikarim.studentassistant.ui.settings.SettingsScreen

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
            val settingsViewModel: SettingsViewModel = viewModel()
            SettingsScreen(viewModel = settingsViewModel)
        }
    }
}
