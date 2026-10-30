package com.king.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                KingScreen(
                    onStartBubble = { startBubble() },
                    onAskOverlay = { askOverlayPermission() }
                )
            }
        }
    }

    private fun startBubble() {
        if (Settings.canDrawOverlays(this)) {
            startForegroundService(Intent(this, FloatingBubbleService::class.java))
        } else {
            askOverlayPermission()
        }
    }

    private fun askOverlayPermission() {
        startActivity(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
        )
    }
}

@Composable
fun KingScreen(onStartBubble: () -> Unit, onAskOverlay: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("King", style = MaterialTheme.typography.headlineLarge)
        Text("Messagerie intelligente — version de départ", modifier = Modifier.padding(top = 8.dp))
        Button(onClick = onAskOverlay, modifier = Modifier.padding(top = 24.dp)) {
            Text("Autoriser l'affichage par-dessus les autres applis")
        }
        Button(onClick = onStartBubble, modifier = Modifier.padding(top = 12.dp)) {
            Text("Activer la bulle flottante")
        }
    }
}
