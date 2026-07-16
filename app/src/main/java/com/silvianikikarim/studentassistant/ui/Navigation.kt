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
            OrarioScreen(navController)
        }

        composable(Routes.APPUNTI) {
            AppuntiScreen(navController)
        }

        composable(Routes.CALENDARIO_STUDIO) {
            CalendarioStudioScreen(navController)
        }

        composable(Routes.ANDAMENTO) {
            AndamentoScreen(votoViewModel, navController)
        }

        composable(Routes.CONSIGLI) {
            ConsigliScreen(navController)
        }

        composable(Routes.IMPOSTAZIONI) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val settingsDataStore = com.silvianikikarim.studentassistant.util.SettingsDataStore(context)
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = com.silvianikikarim.studentassistant.viewmodel.SettingsViewModelFactory(settingsDataStore)
            )
            com.silvianikikarim.studentassistant.ui.settings.SettingsScreen(viewModel = settingsViewModel, navController = navController)
        }

        composable(Routes.PROFILO) { ProfiloScreen(navController) }
        composable(Routes.ESPORTA_DATI) { EsportaDatiScreen(navController) }
        composable(Routes.PRIVACY) { PrivacyScreen(navController) }
        composable(Routes.INFO_APP) { InfoAppScreen(navController) }
    }
}
