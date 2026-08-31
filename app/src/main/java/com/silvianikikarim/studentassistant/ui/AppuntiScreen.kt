package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.ui.theme.BrandRed
import com.silvianikikarim.studentassistant.ui.theme.SurfaceSoft
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModel

/**
 * Schermata "I miei Appunti": elenco delle materie, ognuna delle quali
 * contiene le proprie note (testo, immagini, PDF). Stile allineato a
 * CalendarioStudioScreen: stessa palette (BrandRed / SurfaceSoft), stesse
 * forme arrotondate e stesso linguaggio di card ed empty-state.
 *
 * NOTA: le materie sono un elenco fisso (vedi MaterieCorso.tutte, seminato
 * all'avvio in MainActivity): qui NON è più possibile aggiungerne di nuove
 * a testo libero, per evitare duplicati/refusi rispetto al piano di studi
 * ufficiale. Il metodo AppuntiRepository.inserisciMateria resta disponibile
 * nel codice ma non è più raggiungibile da nessuna schermata.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppuntiScreen(
    navController: NavController,
    appuntiViewModel: AppuntiViewModel
) {
    val materie by appuntiViewModel.tutteLeMaterie.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("I miei Appunti", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                text = "${materie.size} " + if (materie.size == 1) "Materia" else "Materie",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            if (materie.isEmpty()) {
                EmptyMaterieHint()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(materie, key = { it.id }) { materia ->
                        MateriaRowCard(
                            materia = materia,
                            onClick = { navController.navigate(Routes.appuntiMateria(materia.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MateriaRowCard(
    materia: Materia,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BrandRed.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = BrandRed
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = materia.nome,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyMaterieHint() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(BrandRed.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = BrandRed
                )
            }
            Spacer(Modifier.height(12.dp))
            Text("Nessuna materia trovata.", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Le materie del piano di studi vengono caricate automaticamente all'avvio dell'app.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}