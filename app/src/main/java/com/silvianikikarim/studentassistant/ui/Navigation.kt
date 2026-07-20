package com.silvianikikarim.studentassistant.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModel
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel
import com.silvianikikarim.studentassistant.viewmodel.SettingsViewModel
import com.silvianikikarim.studentassistant.ui.settings.SettingsScreen

/**
 * AppNavigation
 * "cuore"  app (il router).
 * Jetpack Compose utilizza il NavController per gestire lo spostamento tra le schermate
 * senza dover creare multiple Activity. Ogni schermata è un "composable" a cui assegniamo una rotta (stringa).
 */
@Composable
fun AppNavigation(
    votoViewModel: VotoViewModel,
    appuntiViewModel: AppuntiViewModel
) {
    // Inizializziamo il controller della navigazione
    val navController = rememberNavController()

    // Il NavHost contiene l'albero di tutte le destinazioni possibili.
    // startDestination indica quale schermata aprire all'avvio dell'app.
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
            AppuntiScreen(navController, appuntiViewModel)
        }

        composable(
            route = Routes.APPUNTI_MATERIA,
            arguments = listOf(navArgument("materiaId") { type = NavType.LongType })
        ) { backStackEntry ->
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
            val materiaId = backStackEntry.arguments?.getLong("materiaId") ?: 0L
            val notaId = backStackEntry.arguments?.getLong("notaId") ?: Routes.NOTA_ID_NUOVA
            NotaTestoEditorScreen(materiaId, notaId, navController, appuntiViewModel)
        }

        // ---- SEZIONE CALENDARIO ----
        composable(Routes.CALENDARIO_STUDIO) {
            CalendarioStudioScreen(navController)
        }

        composable(Routes.ANDAMENTO) {
            AndamentoScreen(votoViewModel, navController)
        }

        composable(Routes.CONSIGLI) {
            ConsigliScreen(navController)
        }

        // ---- SEZIONE IMPOSTAZIONI ----
        // Per le impostazioni, recuperiamo il SettingsDataStore (che legge le preferenze salvate in locale, es. Dark Mode)
        // e lo passiamo al SettingsViewModel.
        composable(Routes.IMPOSTAZIONI) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val settingsDataStore = com.silvianikikarim.studentassistant.util.SettingsDataStore(context)
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = com.silvianikikarim.studentassistant.viewmodel.SettingsViewModelFactory(settingsDataStore)
            )
            com.silvianikikarim.studentassistant.ui.settings.SettingsScreen(
                settingsViewModel = settingsViewModel, 
                votoViewModel = votoViewModel,
                navController = navController
            )
        }

        composable(Routes.PROFILO) { ProfiloScreen(navController) }
        composable(Routes.ESPORTA_DATI) { EsportaDatiScreen(navController) }
        composable(Routes.PRIVACY) { PrivacyScreen(navController) }
        composable(Routes.INFO_APP) { InfoAppScreen(navController) }
    }
}