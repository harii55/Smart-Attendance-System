package network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.time.Instant

class WebSocketClient(private val token: String) {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    suspend fun connect() {
        try {
            client.webSocket(
                urlString = "ws://localhost:8000/attendance/wifi/ws"
            ) {
                println("✅ WebSocket Connected at ${Instant.now()}")

                // Send presence every 5 seconds
                while (true) {
                    send(Frame.Text("""{"email": "jenish.24bcs10046@sst.scaler.com", "bssid": "${bssid.BssidFetcher.getBssid()}"}"""))
                    delay(5000)
                }
            }
        } catch (e: Exception) {
            println("❌ WebSocket Error: ${e.message}")
        }
    }
}
