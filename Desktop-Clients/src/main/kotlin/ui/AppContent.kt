package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import network.WebSocketClient
import bssid.BssidFetcher
import bssid.BssidFetcherFactory

@Composable
fun AppContent() {
    var email by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Idle") }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Smart Attendance Client", style = MaterialTheme.typography.h6)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your student email") },
                singleLine = true
            )

            Button(onClick = {
                if (email.isBlank()) {
                    status = "❌ Please enter a valid email"
                    return@Button
                }

                scope.launch {
                    status = "🔄 Connecting..."
                    val bssid = BssidFetcherFactory.getBssidFetcher().getBssid()
                    if (bssid == null) {
                        status = "❌ Unable to fetch BSSID"
                        return@launch
                    }

                    val client = WebSocketClient(email, bssid)
                    client.connect() // this should include looped pinging logic
                    status = "✅ Connected"
                }
            }) {
                Text("Start Attendance")
            }

            Text("Status: $status")
        }
    }
}
