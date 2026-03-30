package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

private val BrandRed = Color(0xFFAF2A2D)
private val SurfaceSoft = Color(0xFFF6F6F7)

private data class StudyEvent(
    val id: String,
    val date: LocalDate,
    val title: String,
    val start: String,   // "13:00"
    val end: String,     // "18:00"
    val type: String     // "Studio", "Esame", "Ripasso", ecc.
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioStudioScreen() {
    var shownMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // ---- EVENTI (ORA MUTABILI) ----
    var allEvents by remember {
        mutableStateOf(
            listOf(
                StudyEvent(UUID.randomUUID().toString(), LocalDate.now(), "Studio Sistemi Mobili", "13:00", "18:00", "Studio"),
                StudyEvent(UUID.randomUUID().toString(), LocalDate.now().plusDays(1), "Ripasso Reti", "10:00", "12:00", "Ripasso"),
                StudyEvent(UUID.randomUUID().toString(), LocalDate.now().plusDays(3), "Esame Analisi", "09:00", "11:00", "Esame")
            )
        )
    }

    // ---- DIALOG STATE ----
    var showAddDialog by remember { mutableStateOf(false) }

    val eventsByDate = remember(allEvents) { allEvents.groupBy { it.date } }

    val monthEventsCount = remember(shownMonth, allEvents) {
        allEvents.count { YearMonth.from(it.date) == shownMonth }
    }

    val selectedEvents = remember(selectedDate, eventsByDate) {
        (eventsByDate[selectedDate] ?: emptyList()).sortedBy { it.start }
    }

    // ---- DIALOG UI ----
    if (showAddDialog) {
        AddStudyEventDialog(
            date = selectedDate,
            brandColor = BrandRed,
            onDismiss = { showAddDialog = false },
            onSave = { title, type, start, end ->
                val newEv = StudyEvent(
                    id = UUID.randomUUID().toString(),
                    date = selectedDate,
                    title = title.trim(),
                    start = start,
                    end = end,
                    type = type
                )
                allEvents = allEvents + newEv
                showAddDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = { /* TODO menu */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = BrandRed,
                contentColor = Color.White,
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Aggiungi evento")
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
                text = "$monthEventsCount Scheduled",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    MonthHeader(
                        month = shownMonth,
                        onPrev = {
                            shownMonth = shownMonth.minusMonths(1)
                            selectedDate = clampSelectedDateToMonth(selectedDate, shownMonth)
                        },
                        onNext = {
                            shownMonth = shownMonth.plusMonths(1)
                            selectedDate = clampSelectedDateToMonth(selectedDate, shownMonth)
                        }
                    )

                    Spacer(Modifier.height(10.dp))
                    WeekdaysRow()
                    Spacer(Modifier.height(8.dp))

                    MonthGrid(
                        month = shownMonth,
                        selectedDate = selectedDate,
                        hasEvents = { date -> eventsByDate.containsKey(date) },
                        onDateClick = { clicked ->
                            selectedDate = clicked
                            shownMonth = YearMonth.from(clicked)
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Eventi • ${selectedDate.dayOfMonth} ${
                    shownMonth.month.getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
                        .replaceFirstChar { it.uppercase() }
                }",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(10.dp))

            if (selectedEvents.isEmpty()) {
                EmptyEventsHint(
                    onAdd = { showAddDialog = true }
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(selectedEvents, key = { it.id }) { ev ->
                        EventRowCard(event = ev)
                    }
                }
            }
        }
    }
}

@Composable
private fun AddStudyEventDialog(
    date: LocalDate,
    brandColor: Color,
    onDismiss: () -> Unit,
    onSave: (title: String, type: String, start: String, end: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Studio") }
    var start by remember { mutableStateOf("13:00") }
    var end by remember { mutableStateOf("18:00") }

    val typeOptions = listOf("Studio", "Ripasso", "Esame")

    // Validazione semplice
    val canSave = title.trim().isNotEmpty() && isValidTime(start) && isValidTime(end)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { if (canSave) onSave(title, type, start, end) },
                enabled = canSave
            ) {
                Text("Salva", color = if (canSave) brandColor else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annulla") }
        },
        title = {
            Text(
                "Nuovo evento",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Data: ${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.FULL, Locale.ITALIAN)} ${date.year}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titolo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                TypeDropdown(
                    label = "Tipo",
                    options = typeOptions,
                    selected = type,
                    onSelected = { type = it }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = start,
                        onValueChange = { start = it },
                        label = { Text("Inizio (HH:MM)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = end,
                        onValueChange = { end = it },
                        label = { Text("Fine (HH:MM)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (!isValidTime(start) || !isValidTime(end)) {
                    Text(
                        "Formato ora non valido. Esempio: 09:30",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TypeDropdown(
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
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
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

@Composable
private fun MonthHeader(month: YearMonth, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onPrev) {
            Icon(Icons.Filled.ChevronLeft, contentDescription = "Mese precedente")
        }
        Text(
            text = "${month.month.getDisplayName(TextStyle.FULL, Locale.ITALIAN).replaceFirstChar { it.uppercase() }} ${month.year}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = onNext) {
            Icon(Icons.Filled.ChevronRight, contentDescription = "Mese successivo")
        }
    }
}

@Composable
private fun WeekdaysRow() {
    val labels = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
    Row(Modifier.fillMaxWidth()) {
        labels.forEach { d ->
            Text(
                text = d,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MonthGrid(
    month: YearMonth,
    selectedDate: LocalDate,
    hasEvents: (LocalDate) -> Boolean,
    onDateClick: (LocalDate) -> Unit
) {
    val days = remember(month) { buildMonthGridDays(month) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (week in days.chunked(7)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { day ->
                    DayCell(
                        day = day,
                        inMonth = YearMonth.from(day) == month,
                        selected = day == selectedDate,
                        showDot = hasEvents(day),
                        onClick = { onDateClick(day) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: LocalDate,
    inMonth: Boolean,
    selected: Boolean,
    showDot: Boolean,
    onClick: () -> Unit
) {
    val dayColor = when {
        selected -> Color.White
        inMonth -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
    }
    val bg = if (selected) BrandRed else Color.Transparent

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(bg)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.dayOfMonth.toString(),
                color = dayColor,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
            if (showDot) {
                Spacer(Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(if (selected) Color.White else BrandRed)
                )
            }
        }
    }
}

@Composable
private fun EventRowCard(event: StudyEvent) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BrandRed.copy(alpha = 0.08f))
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = event.date.dayOfMonth.toString(),
                    color = BrandRed,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = event.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${event.start} - ${event.end}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AssistChip(
                onClick = { },
                label = { Text(event.type) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = BrandRed.copy(alpha = 0.10f),
                    labelColor = BrandRed
                ),
                border = null
            )
        }
    }
}

@Composable
private fun EmptyEventsHint(onAdd: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Nessun evento per questa data.", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Tocca + per aggiungere una sessione di studio o un esame.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onAdd) { Text("Aggiungi evento", color = BrandRed) }
        }
    }
}

/** Griglia 6x7 (42 celle) con partenza da Lunedì */
private fun buildMonthGridDays(month: YearMonth): List<LocalDate> {
    val firstOfMonth = month.atDay(1)
    val shift = ((firstOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value) + 7) % 7
    val start = firstOfMonth.minusDays(shift.toLong())
    return (0 until 42).map { start.plusDays(it.toLong()) }
}

private fun clampSelectedDateToMonth(selected: LocalDate, month: YearMonth): LocalDate {
    return if (YearMonth.from(selected) == month) selected else month.atDay(1)
}

private fun isValidTime(value: String): Boolean {
    // accetta "HH:MM" 00-23 e 00-59
    val parts = value.trim().split(":")
    if (parts.size != 2) return false
    val h = parts[0].toIntOrNull() ?: return false
    val m = parts[1].toIntOrNull() ?: return false
    return h in 0..23 && m in 0..59 && parts[0].length == 2 && parts[1].length == 2
}
