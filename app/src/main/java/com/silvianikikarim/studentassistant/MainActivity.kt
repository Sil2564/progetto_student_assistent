package com.silvianikikarim.studentassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silvianikikarim.studentassistant.model.AppuntiDatabase
import com.silvianikikarim.studentassistant.model.VotoDatabase
import com.silvianikikarim.studentassistant.repository.AppuntiRepository
import com.silvianikikarim.studentassistant.repository.VotoRepository
import com.silvianikikarim.studentassistant.ui.*
import com.silvianikikarim.studentassistant.ui.theme.StudentAssistantTheme
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModel
import com.silvianikikarim.studentassistant.viewmodel.AppuntiViewModelFactory
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModel
import com.silvianikikarim.studentassistant.viewmodel.VotoViewModelFactory

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

        setContent {
            StudentAssistantTheme {
                val votoViewModel: VotoViewModel = viewModel(factory = votoFactory)
                val appuntiViewModel: AppuntiViewModel = viewModel(factory = appuntiFactory)
                AppNavigation(
                    votoViewModel = votoViewModel,
                    appuntiViewModel = appuntiViewModel
                )
            }
        }
    }
}
