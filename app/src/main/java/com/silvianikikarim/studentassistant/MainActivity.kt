package com.silvianikikarim.studentassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silvianikikarim.studentassistant.model.AppuntiDatabase
import com.silvianikikarim.studentassistant.model.VotoDatabase
import com.silvianikikarim.studentassistant.repository.AppuntiRepository
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val votoDao = VotoDatabase.getDatabase(applicationContext).votoDao()
        val votoRepository = VotoRepository(votoDao)
        val votoFactory = VotoViewModelFactory(votoRepository)

        val appuntiDatabase = AppuntiDatabase.getDatabase(applicationContext)
        val appuntiRepository = AppuntiRepository(
            materiaDao = appuntiDatabase.materiaDao(),
            notaDao = appuntiDatabase.notaDao()
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
