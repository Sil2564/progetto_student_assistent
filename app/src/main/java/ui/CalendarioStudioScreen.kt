package com.silvianikikarim.studentassistant.ui.calendario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silvianikikarim.studentassistant.viewmodel.CalendarioStudioViewModel

@Composable
fun CalendarioStudioScreen(
    viewModel: CalendarioStudioViewModel
) {
    val eventi by viewModel.eventi.collectAsStateWithLifecycle(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Il mio calendario di studio") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* apri dialog */ }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(eventi) { evento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(evento.titolo, style = MaterialTheme.typography.titleMedium)
                        Text("${evento.materia} • ${evento.oraInizio} - ${evento.oraFine}")
                        Text(evento.tipo)
                    }
                }
            }
        }
    }
}
