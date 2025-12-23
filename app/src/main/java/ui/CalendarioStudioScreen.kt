package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioStudioScreen() {

    val eventi = listOf(
        "Studio Sistemi Mobili",
        "Ripasso Reti",
        "Esame Analisi"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Il mio calendario di studio") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: aggiunta evento */ }) {
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
                    Text(
                        text = evento,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
