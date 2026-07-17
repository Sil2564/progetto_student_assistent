package com.silvianikikarim.studentassistant.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.silvianikikarim.studentassistant.model.Nota
import com.silvianikikarim.studentassistant.model.TipoNota
import com.silvianikikarim.studentassistant.util.FileStorageHelper
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaAppuntiScreen(
    materiaId: Long,
    navController: NavController,
    appuntiViewModel: AppuntiViewModel
) {
    val context = LocalContext.current

    val materie by appuntiViewModel.tutteLeMaterie.collectAsState()
    val nomeMateria = materie.firstOrNull { it.id == materiaId }?.nome ?: ""

    val note by appuntiViewModel.noteByMateria(materiaId).collectAsState(initial = emptyList())

    var mostraBottomSheet by remember { mutableStateOf(false) }
    var notaSelezionataPerMenu by remember { mutableStateOf<Nota?>(null) }
    var notaImmagineDaVisualizzare by remember { mutableStateOf<Nota?>(null) }

    // File temporaneo creato prima dello scatto, salvato nel database solo se lo scatto riesce
    var fileFotoInAttesa by remember { mutableStateOf<File?>(null) }

    // --- Launcher: galleria (Photo Picker di sistema, nessun permesso richiesto) ---
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val path = FileStorageHelper.copiaUriInInterno(context, uri, "jpg")
            appuntiViewModel.inserisciNota(
                Nota(
                    materiaId = materiaId,
                    tipo = TipoNota.IMMAGINE,
                    titolo = "Immagine ${formattaData(System.currentTimeMillis())}",
                    percorsoFile = path
                )
            )
        }
    }

    // --- Launcher: PDF (picker di sistema, nessun permesso richiesto) ---
    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val path = FileStorageHelper.copiaUriInInterno(context, uri, "pdf")
            appuntiViewModel.inserisciNota(
                Nota(
                    materiaId = materiaId,
                    tipo = TipoNota.PDF,
                    titolo = "PDF ${formattaData(System.currentTimeMillis())}",
                    percorsoFile = path
                )
            )
        }
    }

    // --- Launcher: fotocamera ---
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { successo: Boolean ->
        val file = fileFotoInAttesa
        if (successo && file != null) {
            appuntiViewModel.inserisciNota(
                Nota(
                    materiaId = materiaId,
                    tipo = TipoNota.IMMAGINE,
                    titolo = "Foto ${formattaData(System.currentTimeMillis())}",
                    percorsoFile = file.absolutePath
                )
            )
        } else {
            file?.delete() // scatto annullato: rimuovo il file vuoto
        }
        fileFotoInAttesa = null
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concesso: Boolean ->
        if (concesso) {
            val (file, uri) = FileStorageHelper.creaFilePerFotocamera(context)
            fileFotoInAttesa = file
            cameraLauncher.launch(uri)
        }
    }

    fun avviaScattoFoto() {
        val giaConcesso = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (giaConcesso) {
            val (file, uri) = FileStorageHelper.creaFilePerFotocamera(context)
            fileFotoInAttesa = file
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(nomeMateria) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostraBottomSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi appunto")
            }
        }
    ) { padding ->
        if (note.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessun appunto. Tocca + per aggiungerne uno.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(note, key = { it.id }) { nota ->
                    NotaRow(
                        nota = nota,
                        onClick = {
                            when (nota.tipo) {
                                TipoNota.TESTO -> navController.navigate(Routes.appuntiNota(materiaId, nota.id))
                                TipoNota.IMMAGINE -> notaImmagineDaVisualizzare = nota
                                TipoNota.PDF -> apriPdfEsterno(context, nota)
                            }
                        },
                        onLongClick = { notaSelezionataPerMenu = nota }
                    )
                }
            }
        }
    }

    if (mostraBottomSheet) {
        BottomSheetAggiungiAppunto(
            onDismiss = { mostraBottomSheet = false },
            onTesto = {
                mostraBottomSheet = false
                navController.navigate(Routes.appuntiNota(materiaId))
            },
            onGalleria = {
                mostraBottomSheet = false
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onFotocamera = {
                mostraBottomSheet = false
                avviaScattoFoto()
            },
            onPdf = {
                mostraBottomSheet = false
                pdfLauncher.launch(arrayOf("application/pdf"))
            }
        )
    }

    notaSelezionataPerMenu?.let { nota ->
        MenuContestualeNota(
            nota = nota,
            onModifica = {
                notaSelezionataPerMenu = null
                navController.navigate(Routes.appuntiNota(materiaId, nota.id))
            },
            onElimina = {
                appuntiViewModel.eliminaNota(nota)
                notaSelezionataPerMenu = null
            },
            onDismiss = { notaSelezionataPerMenu = null }
        )
    }

    notaImmagineDaVisualizzare?.let { nota ->
        Dialog(onDismissRequest = { notaImmagineDaVisualizzare = null }) {
            nota.percorsoFile?.let { path ->
                AsyncImage(
                    model = File(path),
                    contentDescription = nota.titolo,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NotaRow(
    nota: Nota,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icona = when (nota.tipo) {
                TipoNota.TESTO -> Icons.AutoMirrored.Filled.Article
                TipoNota.IMMAGINE -> Icons.Default.Image
                TipoNota.PDF -> Icons.Default.PictureAsPdf
            }
            Icon(icona, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(nota.titolo, style = MaterialTheme.typography.titleMedium)
                Text(
                    formattaData(nota.dataModifica),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetAggiungiAppunto(
    onDismiss: () -> Unit,
    onTesto: () -> Unit,
    onGalleria: () -> Unit,
    onFotocamera: () -> Unit,
    onPdf: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                "Aggiungi appunto",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            OpzioneBottomSheet(Icons.AutoMirrored.Filled.Article, "Nota di testo", onTesto)
            OpzioneBottomSheet(Icons.Default.PhotoLibrary, "Foto dalla galleria", onGalleria)
            OpzioneBottomSheet(Icons.Default.PhotoCamera, "Scatta foto", onFotocamera)
            OpzioneBottomSheet(Icons.Default.PictureAsPdf, "Carica PDF", onPdf)
        }
    }
}

@Composable
private fun OpzioneBottomSheet(icon: ImageVector, testo: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Text(testo)
    }
}

@Composable
private fun MenuContestualeNota(
    nota: Nota,
    onModifica: () -> Unit,
    onElimina: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(nota.titolo) },
        text = { Text("Cosa vuoi fare con questo appunto?") },
        confirmButton = {
            if (nota.tipo == TipoNota.TESTO) {
                TextButton(onClick = onModifica) { Text("Modifica") }
            }
        },
        dismissButton = {
            TextButton(onClick = onElimina) { Text("Elimina") }
        }
    )
}

private fun apriPdfEsterno(context: Context, nota: Nota) {
    val path = nota.percorsoFile ?: return
    val file = File(path)
    val uri = FileStorageHelper.getUriPerFile(context, file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(intent)
}

private fun formattaData(timestampMillis: Long): String {
    val formatter = SimpleDateFormat("dd/MM HH:mm", Locale.ITALY)
    return formatter.format(Date(timestampMillis))
}
