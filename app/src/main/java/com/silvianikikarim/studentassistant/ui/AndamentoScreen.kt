package com.silvianikikarim.studentassistant.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ChevronRight
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
import java.util.Locale

/**
 * Schermata "Andamento": elenco delle materie dell'anno (le stesse mostrate in
 * Orario e in Appunti — nessuna creazione libera qui), ognuna con la propria
 * media locale. Si tocca una materia per entrare ed aggiungere/vedere i voti
 * di QUELLA materia (vedi MateriaAndamentoScreen), esattamente come in Appunti
 * si tocca una materia per vedere le sue note.
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

    // Raggruppa i voti per materia, per calcolare media e conteggio locali di ogni card.
    val votiPerMateriaId = remember(votiConMateria) {
        votiConMateria.groupBy { it.voto.materiaId }
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
                        val votiMateria = votiPerMateriaId[materia.id].orEmpty()
                        MateriaAndamentoRowCard(
                            materia = materia,
                            votiMateria = votiMateria,
                            onClick = { navController.navigate(Routes.andamentoMateria(materia.id)) }
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
    votiMateria: List<VotoConMateria>,
    onClick: () -> Unit
) {
    val mediaLocale = remember(votiMateria) {
        if (votiMateria.isEmpty()) null
        else votiMateria.map { valoreNumerico(it.voto.voto) }.average().toFloat()
    }

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
                    text = if (votiMateria.isEmpty()) {
                        "Nessun voto ancora"
                    } else {
                        "${votiMateria.size} " + (if (votiMateria.size == 1) "voto" else "voti") +
                            " • media ${String.format(Locale.ITALIAN, "%.1f", mediaLocale)}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
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

/** 31 è la codifica interna di "30 e Lode" (nessuna modifica allo schema Room). */
internal fun valoreNumerico(voto: Int): Int = if (voto == 31) 30 else voto

internal fun etichettaVoto(voto: Int): String = if (voto == 31) "30L" else voto.toString()

internal fun etichettaVotoEstesa(voto: Int): String = if (voto == 31) "30 e Lode" else voto.toString()
