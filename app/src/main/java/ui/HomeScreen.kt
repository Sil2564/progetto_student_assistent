package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Benvenuto nella Home!")

        Button(onClick = { navController.navigate("orario") }) {
            Text("Orario lezioni")
        }

        Button(onClick = { navController.navigate("appunti") }) {
            Text("I miei appunti")
        }

        Button(onClick = { navController.navigate("calendario") }) {
            Text("Calendario Studio")
        }

        Button(onClick = { navController.navigate("andamento") }) {
            Text("Andamento")
        }

        Button(onClick = { navController.navigate("consigli") }) {
            Text("Consigli Studio")
        }

        Button(onClick = { navController.navigate("impostazioni") }) {
            Text("Impostazioni")
        }
    }
}
