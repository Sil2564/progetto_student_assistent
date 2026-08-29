package com.silvianikikarim.studentassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silvianikikarim.studentassistant.model.AppDatabase
import com.silvianikikarim.studentassistant.model.MaterieAnnoCorrente
import com.silvianikikarim.studentassistant.repository.AppuntiRepository
import com.silvianikikarim.studentassistant.repository.MateriaRepository
import com.silvianikikarim.studentassistant.repository.VotoRepository
import com.silvianikikarim.studentassistant.repository.ConsigliRepository
import com.silvianikikarim.studentassistant.network.ZenQuotesApi
import com.silvianikikarim.studentassistant.util.FrasedelGiornoCache
import com.silvianikikarim.studentassistant.ui.*
import com.silvianikikarim.studentassistant.ui.theme.StudentAssistantTheme
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModel
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModelFactory
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModelFactory
import com.silvianikikarim.studentassistant.viewmodel.ConsigliViewModel
import com.silvianikikarim.studentassistant.viewmodel.ConsigliViewModelFactory
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.silvianikikarim.studentassistant.util.SettingsDataStore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Un solo database per tutta l'app: Materia è condivisa da Appunti e
        // Andamento (e in futuro anche dall'Orario), niente più liste separate.
        val appDatabase = AppDatabase.getDatabase(applicationContext)
        val materiaRepository = MateriaRepository(appDatabase.materiaDao())

        // Le materie "ufficiali" dell'anno (le stesse dell'Orario) vengono
        // create una volta sola all'avvio: da qui in poi Andamento e Appunti
        // le trovano già pronte, senza che l'utente debba inserirle a mano.
        // Richiamarlo ad ogni avvio non crea duplicati (vedi getOrCreateMateria).
        lifecycleScope.launch {
            materiaRepository.seedMaterieSeNecessario(MaterieAnnoCorrente.nomi)
        }

        val votoRepository = VotoRepository(
            votoDao = appDatabase.votoDao(),
            materiaRepository = materiaRepository
        )
        val votoFactory = VotoViewModelFactory(votoRepository)

        val appuntiRepository = AppuntiRepository(
            materiaRepository = materiaRepository,
            notaDao = appDatabase.notaDao()
        )
        val appuntiFactory = AppuntiViewModelFactory(appuntiRepository)

        val consigliRepository = ConsigliRepository(
            api = ZenQuotesApi.create(),
            cache = FrasedelGiornoCache(applicationContext)
        )
        val consigliFactory = ConsigliViewModelFactory(consigliRepository)

        val settingsDataStore = SettingsDataStore(applicationContext)

        setContent {
            val darkMode by settingsDataStore.darkModeFlow.collectAsState(initial = isSystemInDarkTheme())
            StudentAssistantTheme(darkTheme = darkMode) {
                val votoViewModel: VotoViewModel = viewModel(factory = votoFactory)
                val appuntiViewModel: AppuntiViewModel = viewModel(factory = appuntiFactory)
                val consigliViewModel: ConsigliViewModel = viewModel(factory = consigliFactory)
                AppNavigation(
                    votoViewModel = votoViewModel,
                    appuntiViewModel = appuntiViewModel,
                    consigliViewModel = consigliViewModel
                )
            }
        }
    }
}
