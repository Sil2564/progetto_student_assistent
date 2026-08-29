package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.silvianikikarim.studentassistant.model.Voto
import com.silvianikikarim.studentassistant.ui.theme.BrandRed
import com.silvianikikarim.studentassistant.ui.theme.SurfaceSoft
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Schermata "voti di una materia": la materia è fissa (arriva dalla navigazione,
 * scelta dall'elenco in AndamentoScreen), quindi qui non si digita/sceglie mai
 * il nome materia — si aggiungono solo voto, data, tipologia e note per QUESTA
 * materia. Stessa struttura a due livelli di Appunti (MateriaAppuntiScreen).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaAndamentoScreen(
    materiaId: Long,
    navController: NavController,
    viewModel: VotoViewModel
) {
    val materie by viewModel.tutteLeMaterie.collectAsState()
    val nomeMateria = materie.firstOrNull { it.id == materiaId }?.nome ?: ""

    val voti by viewModel.votiByMateria(materiaId).collectAsState(initial = emptyList())

    var showAddSheet by remember { mutableStateOf(false) }
    var votoDaEliminare by remember { mutableStateOf<Voto?>(null) }

    if (showAddSheet) {
        AggiungiVotoBottomSheet(
            onDismiss = { showAddSheet = false },
            onSave = { votoValore, dataText, tipologia, note ->
                viewModel.inserisciVoto(materiaId, votoValore, dataText, tipologia, note)
            }
        )
    }

    votoDaEliminare?.let { voto ->
        AlertDialog(
            onDismissRequest = { votoDaEliminare = null },
            title = { Text("Eliminare questo voto?") },
            text = { Text("$nomeMateria — ${etichettaVotoEstesa(voto.voto)}") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminaVoto(voto)
                    votoDaEliminare = null
                }) { Text("Elimina", color = BrandRed) }
            },
            dismissButton = {
                TextButton(onClick = { votoDaEliminare = null }) { Text("Annulla") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(nomeMateria, fontWeight = FontWeight.SemiBold) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = BrandRed,
                contentColor = Color.White,
                onClick = { showAddSheet = true }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Aggiungi voto")
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
                text = "${voti.size} " + if (voti.size == 1) "voto registrato" else "voti registrati",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            if (voti.isEmpty()) {
                EmptyVotiHint(onAdd = { showAddSheet = true })
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(voti, key = { it.id }) { voto ->
                        VotoRowCard(
                            voto = voto,
                            onLongClick = { votoDaEliminare = voto }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VotoRowCard(voto: Voto, onLongClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {}, onLongClick = onLongClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BrandRed.copy(alpha = 0.10f))
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = etichettaVoto(voto.voto),
                    color = BrandRed,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "/ 30",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandRed.copy(alpha = 0.7f)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = "${voto.data} • ${voto.descrizione}",
                    fontWeight = FontWeight.SemiBold
                )
                if (voto.note.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = voto.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyVotiHint(onAdd: () -> Unit) {
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
                Icon(Icons.Default.Grade, contentDescription = null, tint = BrandRed)
            }
            Spacer(Modifier.height(12.dp))
            Text("Nessun voto per questa materia.", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Tocca + per registrare il tuo primo voto in questa materia.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onAdd) { Text("Aggiungi voto", color = BrandRed) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AggiungiVotoBottomSheet(
    onDismiss: () -> Unit,
    onSave: (voto: Int, data: String, descrizione: String, note: String) -> Unit
) {
    var votoLabel by remember { mutableStateOf("") }
    var tipologia by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var dataText by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val votoOptions = remember { (18..30).map { it.toString() } + "30 e Lode" }
    val tipologiaOptions = listOf("Orale", "Scritto", "Pratico")

    val canSave = votoLabel.isNotEmpty() && dataText.isNotEmpty() && tipologia.isNotEmpty()

    @Composable
    fun campoColors() = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandRed,
        focusedLabelColor = BrandRed,
        cursorColor = BrandRed
    )

    fun salvaEChiudi() {
        if (!canSave) return
        val votoValore = if (votoLabel == "30 e Lode") 31 else votoLabel.toIntOrNull() ?: return
        onSave(votoValore, dataText, tipologia, note.trim())
        onDismiss()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Aggiungi voto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            CampoDropdown(
                label = "Voto",
                options = votoOptions,
                selected = votoLabel,
                onSelected = { votoLabel = it }
            )

            Spacer(Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = dataText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Data") },
                    trailingIcon = {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = BrandRed)
                    },
                    colors = campoColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }

            Spacer(Modifier.height(12.dp))

            CampoDropdown(
                label = "Tipologia prova",
                options = tipologiaOptions,
                selected = tipologia,
                onSelected = { tipologia = it }
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (opzionale)") },
                colors = campoColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { salvaEChiudi() },
                enabled = canSave,
                colors = ButtonDefaults.buttonColors(containerColor = BrandRed, contentColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Salva voto")
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDateMillis = millis
                        dataText = formattaDataMillis(millis)
                    }
                    showDatePicker = false
                }) { Text("OK", color = BrandRed) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Annulla") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampoDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandRed,
                focusedLabelColor = BrandRed,
                cursorColor = BrandRed
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = {
                        onSelected(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun formattaDataMillis(millis: Long): String {
    val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALIAN))
}
