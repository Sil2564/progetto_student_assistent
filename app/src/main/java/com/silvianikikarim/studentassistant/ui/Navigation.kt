package com.silvianikikarim.studentassistant.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.silvianikikarim.studentassistant.model.AppuntiDatabase
import com.silvianikikarim.studentassistant.repository.AppuntiRepository
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModel
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModelFactory
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
            val context = LocalContext.current
            val database = AppuntiDatabase.getDatabase(context)
            val appuntiViewModel: AppuntiViewModel = viewModel(
                factory = AppuntiViewModelFactory(
                    AppuntiRepository(database.materiaDao(), database.notaDao())
                )
            )
            AppuntiScreen(navController, appuntiViewModel)
        }

        composable(
            route = Routes.APPUNTI_MATERIA,
            arguments = listOf(navArgument("materiaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val context = LocalContext.current
            val database = AppuntiDatabase.getDatabase(context)
            val appuntiViewModel: AppuntiViewModel = viewModel(
                factory = AppuntiViewModelFactory(
                    AppuntiRepository(database.materiaDao(), database.notaDao())
                )
            )
            val materiaId = backStackEntry.arguments?.getLong("materiaId") ?: 0L
            MateriaAppuntiScreen(materiaId, navController, appuntiViewModel)
        }

        composable(
            route = Routes.APPUNTI_NOTA,
            arguments = listOf(
                navArgument("materiaId") { type = NavType.LongType },
                navArgument("notaId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val context = LocalContext.current
            val database = AppuntiDatabase.getDatabase(context)
            val appuntiViewModel: AppuntiViewModel = viewModel(
                factory = AppuntiViewModelFactory(
                    AppuntiRepository(database.materiaDao(), database.notaDao())
                )
            )
            val materiaId = backStackEntry.arguments?.getLong("materiaId") ?: 0L
            val notaId = backStackEntry.arguments?.getLong("notaId") ?: Routes.NOTA_ID_NUOVA
            NotaTestoEditorScreen(materiaId, notaId, navController, appuntiViewModel)
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
