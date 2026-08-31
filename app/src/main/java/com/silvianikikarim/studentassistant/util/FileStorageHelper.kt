package com.silvianikikarim.studentassistant.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Gestisce il salvataggio fisico di immagini e PDF nello storage interno dell'app
 * (context.filesDir/appunti_files/...), così i file sopravvivono a revoche di permessi
 * o alla cancellazione dell'originale in galleria. Nel database salviamo solo il path
 * assoluto risultante.
 *
 * Tutte le funzioni sono `suspend` e spostano esplicitamente il lavoro su
 * Dispatchers.IO: creare cartelle, aprire stream e copiare byte è I/O bloccante,
 * quindi non deve mai girare sul thread che le chiama (es. Main, nei callback
 * dei launcher di Compose).
 */
object FileStorageHelper {

    private const val CARTELLA_APPUNTI = "appunti_files"

    // Deve corrispondere esattamente ad android:authorities nel Manifest
    private const val AUTHORITY = "com.silvianikikarim.studentassistant.fileprovider"

    private fun getCartellaAppunti(context: Context): File {
        val dir = File(context.filesDir, CARTELLA_APPUNTI)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /**
     * Copia il contenuto di un content:// Uri (scelto da galleria o dal picker PDF)
     * in un nuovo file interno. Ritorna il path assoluto del file salvato.
     */
    suspend fun copiaUriInInterno(context: Context, sourceUri: Uri, estensione: String): String =
        withContext(Dispatchers.IO) {
            val nomeFile = "${UUID.randomUUID()}.$estensione"
            val destFile = File(getCartellaAppunti(context), nomeFile)

            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            destFile.absolutePath
        }

    /**
     * Crea un file vuoto destinato alla foto scattata con la fotocamera e ritorna:
     * - il File stesso (il cui path assoluto va salvato nel database)
     * - il suo content:// Uri, ottenuto tramite FileProvider, da passare a
     *   ActivityResultContracts.TakePicture() come destinazione dello scatto.
     */
    suspend fun creaFilePerFotocamera(context: Context): Pair<File, Uri> =
        withContext(Dispatchers.IO) {
            val nomeFile = "${UUID.randomUUID()}.jpg"
            val file = File(getCartellaAppunti(context), nomeFile)
            val uri = FileProvider.getUriForFile(context, AUTHORITY, file)
            file to uri
        }

    /**
     * Ritorna un content:// Uri (tramite FileProvider) per un file già salvato internamente,
     * utile per aprire un PDF con un'app esterna (Intent.ACTION_VIEW). Non fa I/O
     * (solo costruzione dell'Uri), quindi resta una funzione normale, non suspend.
     */
    fun getUriPerFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, AUTHORITY, file)
    }
}
