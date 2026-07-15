package com.silvianikikarim.studentassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silvianikikarim.studentassistant.model.VotoDatabase
import com.silvianikikarim.studentassistant.repository.VotoRepository
import com.silvianikikarim.studentassistant.ui.*
import com.silvianikikarim.studentassistant.ui.theme.StudentAssistantTheme
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModelFactory
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.silvianikikarim.studentassistant.util.SettingsDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val votoDao = VotoDatabase.getDatabase(applicationContext).votoDao()
        val repository = VotoRepository(votoDao)
        val factory = VotoViewModelFactory(repository)
        val settingsDataStore = SettingsDataStore(applicationContext)

        setContent {
            val darkMode by settingsDataStore.darkModeFlow.collectAsState(initial = isSystemInDarkTheme())
            StudentAssistantTheme(darkTheme = darkMode) {
                val votoViewModel: VotoViewModel = viewModel(factory = factory)
                AppNavigation(votoViewModel = votoViewModel)
            }
        }
    }
}
