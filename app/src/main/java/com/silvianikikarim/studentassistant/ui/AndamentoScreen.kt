package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
 * Schermata "Andamento": media voti in evidenza (anello grafico) + elenco.
 * Stile allineato a CalendarioStudioScreen / AppuntiScreen: stessa palette
 * (BrandRed / SurfaceSoft), stesse card arrotondate, FAB rosso che apre un
 * bottom sheet per l'inserimento (come per gli appunti).
 *
 * Nota sulla codifica del voto: essendo il campo "voto" un Int, "30 e Lode"
 * viene salvato come 31 (nessuna modifica allo schema Room necessaria). Nel
 * calcolo della media la lode vale 30, come da prassi.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndamentoScreen(viewModel: VotoViewModel, navController: NavController) {
    val listaVoti by viewModel.tuttiIVoti.collectAsState()

    var showAddSheet by remember { mutableStateOf(false) }
    var votoDaEliminare by remember { mutableStateOf<Voto?>(null) }

    if (showAddSheet) {
        AggiungiVotoBottomSheet(
            onDismiss = { showAddSheet = false },
            onSave = { nuovoVoto -> viewModel.inserisciVoto(nuovoVoto) }
        )
    }

    votoDaEliminare?.let { voto ->
        AlertDialog(
            onDismissRequest = { votoDaEliminare = null },
            title = { Text("Eliminare questo voto?") },
            text = { Text("${voto.materia} — ${etichettaVotoEstesa(voto.voto)}") },
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
                title = { Text("Andamento", fontWeight = FontWeight.SemiBold) },
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

            MediaCard(listaVoti)

            Spacer(Modifier.height(20.dp))

            Text(
                text = "I miei voti",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(10.dp))

            if (listaVoti.isEmpty()) {
                EmptyVotiHint(onAdd = { showAddSheet = true })
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(listaVoti, key = { it.id }) { voto ->
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

@Composable
private fun MediaCard(voti: List<Voto>) {
    val media = remember(voti) {
        if (voti.isEmpty()) 0f else voti.map { valoreNumerico(it.voto) }.average().toFloat()
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(92.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    drawArc(
                        color = BrandRed.copy(alpha = 0.12f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = stroke
                    )
                    if (voti.isNotEmpty()) {
                        val sweep = (media / 30f).coerceIn(0f, 1f) * 360f
                        drawArc(
                            color = BrandRed,
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = stroke
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (voti.isEmpty()) "--" else String.format(Locale.ITALIAN, "%.1f", media),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BrandRed
                    )
                    Text(
                        "/ 30",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.width(20.dp))

            Column {
                Text(
                    "Media generale",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (voti.isEmpty()) {
                        "Nessun voto inserito"
                    } else {
                        "${voti.size} " + if (voti.size == 1) "voto registrato" else "voti registrati"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                Text(voto.materia, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${voto.data} • ${voto.descrizione}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (voto.note.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
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
            Text("Nessun voto ancora.", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Tocca + per registrare il tuo primo voto e iniziare a tracciare la media.",
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
    onSave: (Voto) -> Unit
) {
    var materia by remember { mutableStateOf("") }
    var votoLabel by remember { mutableStateOf("") }
    var tipologia by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var dataText by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val votoOptions = remember { (18..30).map { it.toString() } + "30 e Lode" }
    val tipologiaOptions = listOf("Orale", "Scritto", "Pratico")

    val canSave = materia.trim().isNotEmpty() &&
            votoLabel.isNotEmpty() &&
            dataText.isNotEmpty() &&
            tipologia.isNotEmpty()

    @Composable
    fun campoColors() = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandRed,
        focusedLabelColor = BrandRed,
        cursorColor = BrandRed
    )

    fun salvaEChiudi() {
        if (!canSave) return
        val votoValore = if (votoLabel == "30 e Lode") 31 else votoLabel.toIntOrNull() ?: return
        onSave(
            Voto(
                materia = materia.trim(),
                voto = votoValore,
                data = dataText,
                descrizione = tipologia,
                note = note.trim()
            )
        )
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

            OutlinedTextField(
                value = materia,
                onValueChange = { materia = it },
                label = { Text("Materia") },
                singleLine = true,
                colors = campoColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

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
                // Overlay trasparente: intercetta il tap e apre il calendario
                // (il campo resta readOnly, niente tastiera).
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

/** 31 è la codifica interna di "30 e Lode" (nessuna modifica allo schema Room). */
private fun valoreNumerico(voto: Int): Int = if (voto == 31) 30 else voto

private fun etichettaVoto(voto: Int): String = if (voto == 31) "30L" else voto.toString()

private fun etichettaVotoEstesa(voto: Int): String = if (voto == 31) "30 e Lode" else voto.toString()

private fun formattaDataMillis(millis: Long): String {
    // Il DatePicker restituisce millis UTC a mezzanotte del giorno selezionato:
    // uso ZoneOffset.UTC per evitare lo sfasamento di un giorno col fuso locale.
    val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALIAN))
}