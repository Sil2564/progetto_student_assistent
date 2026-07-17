package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppuntiScreen(
    navController: NavController,
    appuntiViewModel: AppuntiViewModel
) {
    val materie by appuntiViewModel.tutteLeMaterie.collectAsState()
    var mostraDialogAggiungi by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("I miei appunti") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostraDialogAggiungi = true }) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi materia")
            }
        }
    ) { padding ->
        if (materie.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessuna materia. Tocca + per aggiungerne una.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(materie, key = { it.id }) { materia ->
                    MateriaRow(
                        materia = materia,
                        onClick = {
                            navController.navigate(Routes.appuntiMateria(materia.id))
                        },
                        onDelete = { appuntiViewModel.eliminaMateria(materia) }
                    )
                }
            }
        }
    }

    if (mostraDialogAggiungi) {
        DialogNuovaMateria(
            onConferma = { nome ->
                appuntiViewModel.inserisciMateria(nome)
                mostraDialogAggiungi = false
            },
            onAnnulla = { mostraDialogAggiungi = false }
        )
    }
}

@Composable
private fun MateriaRow(
    materia: Materia,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.MenuBook, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(materia.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Elimina materia")
            }
        }
    }
}

@Composable
private fun DialogNuovaMateria(
    onConferma: (String) -> Unit,
    onAnnulla: () -> Unit
) {
    var nome by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onAnnulla,
        title = { Text("Nuova materia") },
        text = {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome materia") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (nome.isNotBlank()) onConferma(nome.trim()) },
                enabled = nome.isNotBlank()
            ) { Text("Aggiungi") }
        },
        dismissButton = {
            TextButton(onClick = onAnnulla) { Text("Annulla") }
        }
    )
}
