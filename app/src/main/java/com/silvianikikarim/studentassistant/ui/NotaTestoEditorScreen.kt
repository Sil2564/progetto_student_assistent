package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.silvianikikarim.studentassistant.model.Nota
import com.silvianikikarim.studentassistant.model.TipoNota
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotaTestoEditorScreen(
    materiaId: Long,
    notaId: Long,
    navController: NavController,
    appuntiViewModel: AppuntiViewModel
) {
    val modalitaModifica = notaId != Routes.NOTA_ID_NUOVA

    var titolo by remember { mutableStateOf("") }
    var testo by remember { mutableStateOf("") }
    var caricamentoCompletato by remember { mutableStateOf(!modalitaModifica) }

    // In modalità modifica, carico la nota esistente una sola volta
    LaunchedEffect(notaId) {
        if (modalitaModifica) {
            val notaEsistente = appuntiViewModel.getNotaById(notaId)
            if (notaEsistente != null) {
                titolo = notaEsistente.titolo
                testo = notaEsistente.testo ?: ""
            }
            caricamentoCompletato = true
        }
    }

    fun salvaEEsci() {
        val titoloFinale = titolo.ifBlank { "Senza titolo" }
        if (modalitaModifica) {
            appuntiViewModel.aggiornaNota(
                Nota(
                    id = notaId,
                    materiaId = materiaId,
                    tipo = TipoNota.TESTO,
                    titolo = titoloFinale,
                    testo = testo
                )
            )
        } else {
            appuntiViewModel.inserisciNota(
                Nota(
                    materiaId = materiaId,
                    tipo = TipoNota.TESTO,
                    titolo = titoloFinale,
                    testo = testo
                )
            )
        }
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (modalitaModifica) "Modifica nota" else "Nuova nota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(onClick = { salvaEEsci() }) {
                        Icon(Icons.Default.Check, contentDescription = "Salva")
                    }
                }
            )
        }
    ) { padding ->
        if (!caricamentoCompletato) {
            Box(Modifier.fillMaxSize().padding(padding)) // vuoto durante il caricamento
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = titolo,
                onValueChange = { titolo = it },
                label = { Text("Titolo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = testo,
                onValueChange = { testo = it },
                label = { Text("Contenuto") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
