package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.model.VotoConMateria
import com.silvianikikarim.studentassistant.ui.theme.BrandRed
import com.silvianikikarim.studentassistant.ui.theme.SurfaceSoft
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Schermata "Andamento": elenco delle materie dell'anno (le stesse mostrate in
 * Orario e in Appunti — nessuna creazione libera qui), ognuna con AL MASSIMO
 * un voto assegnato. Si tocca una materia e si apre un popup ("Nuovo voto" /
 * "Modifica voto") per assegnare o cambiare il voto di QUELLA materia —
 * nessuna nuova pagina, tutto resta su questa schermata.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndamentoScreen(viewModel: VotoViewModel, navController: NavController) {
    val materie by viewModel.tutteLeMaterie.collectAsState()
    val votiConMateria by viewModel.votiConMateria.collectAsState()

    // Popola le materie dell'anno la prima volta (idempotente: non duplica se già presenti).
    LaunchedEffect(Unit) {
        viewModel.seedMaterieAnnoCorrente()
    }

    // Al massimo un voto per materia: mappa diretta materiaId -> il suo unico voto (se c'è).
    val votoPerMateriaId = remember(votiConMateria) {
        votiConMateria.associateBy { it.voto.materiaId }
    }

    // Materia su cui si è appena toccato: se non è null, mostriamo il popup del voto.
    var materiaSelezionata by remember { mutableStateOf<Materia?>(null) }

    materiaSelezionata?.let { materia ->
        val votoEsistente = votoPerMateriaId[materia.id]
        VotoBottomSheet(
            nomeMateria = materia.nome,
            votoEsistente = votoEsistente,
            onDismiss = { materiaSelezionata = null },
            onSave = { votoValore, dataText, tipologia, note ->
                viewModel.inserisciVoto(
                    materiaId = materia.id,
                    voto = votoValore,
                    data = dataText,
                    descrizione = tipologia,
                    note = note,
                    idEsistente = votoEsistente?.voto?.id ?: 0
                )
                materiaSelezionata = null
            },
            onElimina = {
                votoEsistente?.let { viewModel.eliminaVoto(it.voto) }
                materiaSelezionata = null
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

            MediaGeneraleCard(votiConMateria)

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Le mie materie",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(10.dp))

            if (materie.isEmpty()) {
                EmptyMaterieHint()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(materie, key = { it.id }) { materia ->
                        MateriaAndamentoRowCard(
                            materia = materia,
                            votoMateria = votoPerMateriaId[materia.id],
                            onClick = { materiaSelezionata = materia }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaGeneraleCard(voti: List<VotoConMateria>) {
    val media = remember(voti) {
        if (voti.isEmpty()) 0f else voti.map { valoreNumerico(it.voto.voto) }.average().toFloat()
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

@Composable
private fun MateriaAndamentoRowCard(
    materia: Materia,
    votoMateria: VotoConMateria?,
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

            Column(Modifier.weight(1f)) {
                Text(
                    text = materia.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (votoMateria == null) {
                        "Tocca per assegnare un voto"
                    } else {
                        "${votoMateria.voto.descrizione} • ${votoMateria.voto.data}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.width(8.dp))

            if (votoMateria != null) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(BrandRed.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = etichettaVoto(votoMateria.voto.voto),
                        color = BrandRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Icon(Icons.Filled.Add, contentDescription = "Assegna voto", tint = BrandRed)
            }
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
                Icon(Icons.Default.Grade, contentDescription = null, tint = BrandRed)
            }
            Spacer(Modifier.height(12.dp))
            Text("Nessuna materia ancora.", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Le materie compariranno qui automaticamente in base all'Orario delle lezioni.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Popup per assegnare/modificare/eliminare l'UNICO voto di una materia.
 * Se votoEsistente è null si comporta come "Nuovo voto" (campi vuoti);
 * altrimenti precompila i campi e mostra anche l'opzione per eliminarlo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VotoBottomSheet(
    nomeMateria: String,
    votoEsistente: VotoConMateria?,
    onDismiss: () -> Unit,
    onSave: (voto: Int, data: String, descrizione: String, note: String) -> Unit,
    onElimina: () -> Unit
) {
    var votoLabel by remember(votoEsistente) {
        mutableStateOf(
            votoEsistente?.voto?.voto?.let { if (it == 31) "30 e Lode" else it.toString() } ?: ""
        )
    }
    var tipologia by remember(votoEsistente) { mutableStateOf(votoEsistente?.voto?.descrizione ?: "") }
    var note by remember(votoEsistente) { mutableStateOf(votoEsistente?.voto?.note ?: "") }
    var dataText by remember(votoEsistente) { mutableStateOf(votoEsistente?.voto?.data ?: "") }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var confermaEliminazione by remember { mutableStateOf(false) }

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
                text = if (votoEsistente == null) "Nuovo voto" else "Modifica voto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = nomeMateria,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
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

            if (votoEsistente != null) {
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { confermaEliminazione = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Elimina voto", color = BrandRed)
                }
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

    if (confermaEliminazione) {
        AlertDialog(
            onDismissRequest = { confermaEliminazione = false },
            title = { Text("Eliminare questo voto?") },
            text = { Text(nomeMateria) },
            confirmButton = {
                TextButton(onClick = {
                    confermaEliminazione = false
                    onElimina()
                }) { Text("Elimina", color = BrandRed) }
            },
            dismissButton = {
                TextButton(onClick = { confermaEliminazione = false }) { Text("Annulla") }
            }
        )
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

/** 31 è la codifica interna di "30 e Lode" (nessuna modifica allo schema Room). */
internal fun valoreNumerico(voto: Int): Int = if (voto == 31) 30 else voto

internal fun etichettaVoto(voto: Int): String = if (voto == 31) "30L" else voto.toString()

internal fun etichettaVotoEstesa(voto: Int): String = if (voto == 31) "30 e Lode" else voto.toString()
