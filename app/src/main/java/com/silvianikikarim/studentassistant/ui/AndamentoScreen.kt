package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.silvianikikarim.studentassistant.model.Voto
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel
import androidx.compose.runtime.collectAsState


@Composable
fun AndamentoScreen(viewModel: VotoViewModel) {
    val listaVoti by viewModel.tuttiIVoti.collectAsState()

    var materia by remember { mutableStateOf(TextFieldValue("")) }
    var voto by remember { mutableStateOf(TextFieldValue("")) }
    var data by remember { mutableStateOf(TextFieldValue("")) }
    var descrizione by remember { mutableStateOf(TextFieldValue("")) } // rinominato
    var note by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "I miei voti",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(listaVoti) { voto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Materia: ${voto.materia}")
                        Text("Voto: ${voto.voto}/30")
                        Text("Data: ${voto.data}")
                        Text("Tipo prova: ${voto.descrizione}") // corretto
                        Text("Note: ${voto.note}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = materia,
            onValueChange = { materia = it },
            label = { Text("Materia") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = voto,
            onValueChange = { voto = it },
            label = { Text("Voto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descrizione,
            onValueChange = { descrizione = it },
            label = { Text("Tipologia prova") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (materia.text.isNotBlank() && voto.text.isNotBlank()) {
                    viewModel.inserisciVoto(
                        Voto(
                            materia = materia.text,
                            voto = voto.text.toIntOrNull() ?: 0,
                            data = data.text,
                            descrizione = descrizione.text, // corretto
                            note = note.text
                        )
                    )

                    // Reset campi
                    materia = TextFieldValue("")
                    voto = TextFieldValue("")
                    data = TextFieldValue("")
                    descrizione = TextFieldValue("")
                    note = TextFieldValue("")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Aggiungi voto")
        }
    }
}
