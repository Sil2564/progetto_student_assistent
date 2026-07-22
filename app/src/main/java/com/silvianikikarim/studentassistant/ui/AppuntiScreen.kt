package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
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
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppuntiScreen(
    navController: NavController,
    appuntiViewModel: AppuntiViewModel
) {
    val materie by appuntiViewModel.tutteLeMaterie.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var materiaDaEliminare by remember { mutableStateOf<Materia?>(null) }

    if (showAddDialog) {
        AddMateriaDialog(
            onDismiss = { showAddDialog = false },
            onSave = { nome ->
                appuntiViewModel.inserisciMateria(nome)
                showAddDialog = false
            }
        )
    }

    materiaDaEliminare?.let { materia ->
        AlertDialog(
            onDismissRequest = { materiaDaEliminare = null },
            title = { Text("Eliminare \"${materia.nome}\"?") },
            text = { Text("Verranno eliminati anche tutti gli appunti contenuti in questa materia.") },
            confirmButton = {
                TextButton(onClick = {
                    appuntiViewModel.eliminaMateria(materia)
                    materiaDaEliminare = null
                }) { Text("Elimina", color = BrandRed) }
            },
            dismissButton = {
                TextButton(onClick = { materiaDaEliminare = null }) { Text("Annulla") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("I miei Appunti", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = BrandRed,
                contentColor = Color.White,
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Aggiungi materia")
            }
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
                EmptyMaterieHint(onAdd = { showAddDialog = true })
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(materie, key = { it.id }) { materia ->
                        MateriaRowCard(
                            materia = materia,
                            onClick = { navController.navigate(Routes.appuntiMateria(materia.id)) },
                            onDelete = { materiaDaEliminare = materia }
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
    onClick: () -> Unit,
    onDelete: () -> Unit
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

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Elimina materia",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyMaterieHint(onAdd: () -> Unit) {
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
            Text("Nessuna materia ancora.", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Crea la tua prima materia per iniziare a raccogliere appunti, foto e PDF.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onAdd) { Text("Aggiungi materia", color = BrandRed) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMateriaDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    val canSave = nome.trim().isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuova materia") },
        text = {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome materia") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandRed,
                    focusedLabelColor = BrandRed,
                    cursorColor = BrandRed
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (canSave) onSave(nome.trim()) },
                enabled = canSave
            ) { Text("Salva", color = if (canSave) BrandRed else MaterialTheme.colorScheme.onSurfaceVariant) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annulla") }
        }
    )
}