package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import network.WebSocketClient

@Composable
fun AppContent() {
    var status by remember { mutableStateOf("Not Connected") }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text("Smart Attendance Client (Linux)")
            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    status = "Connecting..."
                    val ws = WebSocketClient(token = "your_jwt_token")
                    ws.connect()
                }
            }) {
                Text("Start Session")
            }

            Spacer(Modifier.height(16.dp))
            Text("Status: $status")
        }
    }
}
