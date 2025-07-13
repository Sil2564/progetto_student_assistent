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

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("orario") { OrarioScreen() }
        composable("appunti") { AppuntiScreen() }
        composable("calendario") { CalendarioScreen() }
        composable("andamento") { AndamentoScreen(votoViewModel) } // ✅ qui usi il ViewModel
        composable("consigli") { ConsigliScreen() }
        composable("impostazioni") { ImpostazioniScreen() }
    }
}
