package com.silvianikikarim.studentassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.silvianikikarim.studentassistant.ui.HomeScreen
import com.silvianikikarim.studentassistant.ui.AppuntiScreen
import com.silvianikikarim.studentassistant.ui.OrarioScreen
import com.silvianikikarim.studentassistant.ui.CalendarioScreen
import com.silvianikikarim.studentassistant.ui.AndamentoScreen
import com.silvianikikarim.studentassistant.ui.ConsigliScreen
import com.silvianikikarim.studentassistant.ui.ImpostazioniScreen
import com.silvianikikarim.studentassistant.ui.theme.StudentAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentAssistantTheme {
                val navController = rememberNavController()
                NavHostApp(navController)
            }
        }
    }
}

@Composable
fun NavHostApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("orario") { OrarioScreen() }
        composable("appunti") { AppuntiScreen() }
        composable("calendario") { CalendarioScreen() }
        composable("andamento") { AndamentoScreen() }
        composable("consigli") { ConsigliScreen() }
        composable("impostazioni") { ImpostazioniScreen() }

    }
}
