package com.silvianikikarim.studentassistant.ui.settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.silvianikikarim.studentassistant.viewmodel.SettingsViewModel
import androidx.compose.foundation.layout.padding


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val darkMode by viewModel.darkMode.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Impostazioni") }
            )
        }
    ) { padding ->
        ListItem(
            modifier = Modifier.padding(padding),
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
