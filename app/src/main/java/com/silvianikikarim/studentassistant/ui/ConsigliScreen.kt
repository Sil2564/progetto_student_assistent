package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.silvianikikarim.studentassistant.model.ArticoloConsiglio
import com.silvianikikarim.studentassistant.viewmodel.ConsigliViewModel
import com.silvianikikarim.studentassistant.viewmodel.StatoFrase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsigliScreen(navController: NavController, viewModel: ConsigliViewModel) {
    val statoFrase by viewModel.statoFrase.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consigli Studio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FraseDelGiornoCard(
                    stato = statoFrase,
                    onRiprova = { viewModel.caricaFraseDelGiorno() }
                )
            }

            item {
                Text(
                    text = "Consigli di studio",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(viewModel.articoli, key = { it.titolo }) { articolo ->
                ArticoloCard(articolo)
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun FraseDelGiornoCard(stato: StatoFrase, onRiprova: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (stato) {
                is StatoFrase.Caricamento -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is StatoFrase.Disponibile -> {
                    Text(
                        text = "\"${stato.frase.testo}\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "— ${stato.frase.autore}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                is StatoFrase.NonDisponibile -> {
                    Text(
                        text = "Non è stato possibile caricare la frase del giorno. Controlla la connessione e riprova.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRiprova) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Riprova")
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticoloCard(articolo: ArticoloConsiglio) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = articolo.titolo, style = MaterialTheme.typography.titleMedium)
            Text(
                text = articolo.sottotitolo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = articolo.corpo, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
