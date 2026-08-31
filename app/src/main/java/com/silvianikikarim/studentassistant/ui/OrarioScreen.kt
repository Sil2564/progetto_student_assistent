package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.silvianikikarim.studentassistant.ui.theme.BrandRed
import com.silvianikikarim.studentassistant.ui.theme.SurfaceSoft
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

/**
 * Modello dati per le lezioni universitarie.
 */
private data class LessonEvent(
    val id: String,
    val date: LocalDate,
    val title: String,
    val start: String,
    val end: String,
    val room: String
)

/**
 * OrarioScreen
 * Mostra l'orario delle lezioni sotto forma di calendario mensile.
 * Riutilizza la stessa logica di UI del Calendario Studio per massima coerenza.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrarioScreen(navController: NavController, modifier: Modifier = Modifier) {
    var shownMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Genera automaticamente un finto orario del 3° anno basato sul mese corrente.
    val allLessons = remember(shownMonth) { generateFakeLessons(shownMonth) }

    val lessonsByDate = remember(allLessons) { allLessons.groupBy { it.date } }

    val selectedLessons = remember(selectedDate, lessonsByDate) {
        (lessonsByDate[selectedDate] ?: emptyList()).sortedBy { it.start }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orario Lezioni", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandRed, // Sostituito il viola con il rosso del brand!
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Contenitore del Calendario
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
                        hasEvents = { date -> lessonsByDate.containsKey(date) },
                        onDateClick = { clicked ->
                            selectedDate = clicked
                            shownMonth = YearMonth.from(clicked)
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Lezioni • ${selectedDate.dayOfMonth} ${
                    shownMonth.month.getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
                        .replaceFirstChar { it.uppercase() }
                }",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(10.dp))

            if (selectedLessons.isEmpty()) {
                EmptyLessonsHint()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(selectedLessons, key = { it.id }) { lesson ->
                        LessonRowCard(lesson = lesson)
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonRowCard(lesson: LessonEvent) {
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
                    text = lesson.date.dayOfMonth.toString(),
                    color = BrandRed,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = lesson.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(lesson.title, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${lesson.start} - ${lesson.end}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = lesson.room,
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandRed
                )
            }
        }
    }
}

@Composable
private fun EmptyLessonsHint() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Nessuna lezione prevista oggi.", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Goditi il tempo libero o approfittane per ripassare nel Calendario Studio!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
    val labels = listOf("LUN", "MAR", "MER", "GIO", "VEN", "SAB", "DOM")
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

private fun buildMonthGridDays(month: YearMonth): List<LocalDate> {
    val firstOfMonth = month.atDay(1)
    val shift = ((firstOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value) + 7) % 7
    val start = firstOfMonth.minusDays(shift.toLong())
    return (0 until 42).map { start.plusDays(it.toLong()) }
}

private fun clampSelectedDateToMonth(selected: LocalDate, month: YearMonth): LocalDate {
    return if (YearMonth.from(selected) == month) selected else month.atDay(1)
}

/**
 * Genera dinamicamente le lezioni dell'anno corrente basate sul mese visualizzato,
 * rispettando i veri semestri accademici (nessuna lezione a Luglio/Agosto).
 *
 * NOTA: i nomi usati qui sono allineati a MaterieAnnoCorrente.secondoAnno
 * (materie con formato "da lezione settimanale"). Se l'anno corrente
 * dell'utente è diverso, vanno aggiornati qui a mano.
 */
private fun generateFakeLessons(month: YearMonth): List<LessonEvent> {
    val events = mutableListOf<LessonEvent>()

    // Controlliamo se siamo in un mese di lezione (Primo semestre: Sett-Dic, Secondo semestre: Feb-Mag)
    val isFirstSemester = month.monthValue in 9..12
    val isSecondSemester = month.monthValue in 2..5

    // Se siamo nei mesi di pausa (Gennaio, Giugno, Luglio, Agosto), non ci sono lezioni
    if (!isFirstSemester && !isSecondSemester) return emptyList()

    val firstDay = month.atDay(1)
    val lastDay = month.atEndOfMonth()

    var current = firstDay
    while (!current.isAfter(lastDay)) {
        if (isFirstSemester) {
            when (current.dayOfWeek) {
                DayOfWeek.MONDAY -> {
                    events.add(LessonEvent(UUID.randomUUID().toString(), current, "Algoritmi e Strutture Dati", "09:00", "12:00", "Campus Cesena - Aula 2.1"))
                    events.add(LessonEvent(UUID.randomUUID().toString(), current, "Ingegneria dei Sistemi Web", "14:00", "17:00", "Campus Cesena - Aula 1.4"))
                }
                DayOfWeek.WEDNESDAY -> {
                    events.add(LessonEvent(UUID.randomUUID().toString(), current, "Laboratorio di Sistemi di Rete", "10:00", "13:00", "Campus Cesena - Aula 3.1"))
                }
                DayOfWeek.THURSDAY -> {
                    events.add(LessonEvent(UUID.randomUUID().toString(), current, "Laboratorio di Sicurezza dei Sistemi e Privacy", "09:00", "11:00", "Campus Cesena - Lab 1"))
                }
                else -> {}
            }
        } else if (isSecondSemester) {
            when (current.dayOfWeek) {
                DayOfWeek.TUESDAY -> {
                    events.add(LessonEvent(UUID.randomUUID().toString(), current, "Laboratorio di Programmazione di Sistemi Mobili", "09:00", "13:00", "Campus Cesena - Lab 3"))
                }
                DayOfWeek.WEDNESDAY -> {
                    events.add(LessonEvent(UUID.randomUUID().toString(), current, "Progettazione e Sviluppo del Software", "14:00", "17:00", "Campus Cesena - Aula 2.2"))
                }
                DayOfWeek.FRIDAY -> {
                    events.add(LessonEvent(UUID.randomUUID().toString(), current, "Laboratorio di Big Data, Data Mining e Data Analytics", "09:00", "12:00", "Campus Cesena - Aula 1.1"))
                }
                else -> {}
            }
        }
        current = current.plusDays(1)
    }
    return events
}