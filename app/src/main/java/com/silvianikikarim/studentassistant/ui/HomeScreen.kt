package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * HomeScreen
 * Questa è la schermata principale (la dashboard) dell'applicazione.
 * Qui mostriamo le opzioni principali sotto forma di griglia (Card).
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController) {

    // Lista delle voci che vogliamo mostrare nella griglia.
    // Ognuna ha un titolo visibile e la rotta (route) a cui navigare quando viene cliccata.
    val items = listOf(
        HomeItem("Orario lezioni", Routes.ORARIO),
        HomeItem("I miei appunti", Routes.APPUNTI),
        HomeItem("Calendario studio", Routes.CALENDARIO_STUDIO),
        HomeItem("Andamento", Routes.ANDAMENTO),
        HomeItem("Consigli studio", Routes.CONSIGLI),
        HomeItem("Impostazioni", Routes.IMPOSTAZIONI)
    )

    // Lo Scaffold fornisce l'infrastruttura di base per la pagina (es. la barra in alto).
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Student Assistant",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFAF2A2D),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        // Griglia a 2 colonne fisse per le sezioni principali della dashboard
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                HomeCard(
                    title = item.title,
                    onClick = { navController.navigate(item.route) }
                )
            }
        }
    }
}

/**
 * Card quadrata per la griglia della Home.
 */
@Composable
fun HomeCard(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

data class HomeItem(
    val title: String,
    val route: String
)
