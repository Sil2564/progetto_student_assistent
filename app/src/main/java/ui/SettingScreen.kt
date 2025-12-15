package com.silvianikikarim.studentassistant.ui.settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silvianikikarim.studentassistant.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val darkMode by viewModel.darkMode.collectAsStateWithLifecycle(false)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Impostazioni") })
        }
    ) {
        ListItem(
            headlineContent = { Text("Dark mode") },
            trailingContent = {
                Switch(
                    checked = darkMode,
                    onCheckedChange = { viewModel.toggleDarkMode(it) }
                )
            }
        )
    }
}
