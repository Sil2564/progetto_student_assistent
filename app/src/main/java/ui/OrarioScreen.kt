package com.silvianikikarim.studentassistant.ui

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrarioScreen(modifier: Modifier = Modifier) {
    // Uploading the URL to view
    val url=
        "https://corsi.unibo.it/laurea/TecnologieSistemiInformatici/orario-lezioni?anno=3&curricula=C54-000"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orario Lezioni") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Calling the WebView created in the other file
            WebViewScreen(
                url = url,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// Main WebView Function
@Composable
fun WebViewScreen(url: String, modifier: Modifier = Modifier) {
    // Configuring the Factory AndroidView for the function
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // Setting up primary elements for the WebView
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                //Enabling adaptive layout
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        update = { webView ->
            // Loads a new URL when the composable is updated, for example: a restart of the app
            webView.loadUrl(url)
        },
        modifier = modifier
    )
}
