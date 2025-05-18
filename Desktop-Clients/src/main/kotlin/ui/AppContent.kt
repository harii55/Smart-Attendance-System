package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import network.WebSocketClient
import bssid.BssidFetcherFactory

data class AttendanceState(
    val connected: Boolean = false,
    val bssid: String? = null,
    val connectionSeconds: Int = 0,
    val thresholdReached: Boolean = false,
    val attendanceMarked: Boolean = false,
    val serverStatus: String = "",
    var error: String? = null
)

@Composable
fun AppContent() {
    var email by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Idle") }
    var launchKey by remember { mutableStateOf(0) }

    val attendanceState = remember { mutableStateOf(AttendanceState()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Smart Attendance Client", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Student email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (email.isBlank()) {
                    status = "❌ Enter a valid email"
                    return@Button
                }
                launchKey++
            },
            enabled = !attendanceState.value.connected,
        ) {
            Text("Start Attendance")
        }

        Spacer(modifier = Modifier.height(24.dp))

        StatusSection(attendanceState.value, status)
    }

    LaunchedEffect(launchKey) {
        if (launchKey == 0) return@LaunchedEffect

        status = "🔄 Fetching BSSID..."
        val bssid = BssidFetcherFactory.getBssidFetcher().getBssid()
        if (bssid == null) {
            status = "❌ Unable to fetch BSSID"
            return@LaunchedEffect
        }

        val client = WebSocketClient(
            endpoint = "ws://4.213.5.84:8000/attendance/wifi/ws",
            email = email,
            bssid = bssid,
            onStatusChange = { msg -> status = msg },
            onConnectionUpdate = { seconds ->
                attendanceState.value = attendanceState.value.copy(
                    connectionSeconds = seconds,
                    thresholdReached = seconds >= 2700, // 75% of 1-hour class
                    connected = true,
                    bssid = bssid
                )
            },
            onError = { err ->
                attendanceState.value = attendanceState.value.copy(
                    error = err,
                    connected = false
                )
            }
        )

        client.connect()
    }
}

@Composable
fun StatusSection(state: AttendanceState, status: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Status: $status")

        if (state.connected) {
            state.error = null
            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = (state.connectionSeconds / 3600f).coerceAtMost(1f),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Text("Connected for: ${state.connectionSeconds}s")

            Spacer(modifier = Modifier.height(4.dp))
            when {
                state.attendanceMarked -> Text("✅ Attendance marked!", color = MaterialTheme.colors.primary)
                state.thresholdReached -> Text("🟡 Threshold reached. Awaiting finalization.")
                else -> Text("⏳ Monitoring in progress...")
            }
        }

        if (state.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("❌ Error: ${state.error}", color = MaterialTheme.colors.error)
        }
    }
}
