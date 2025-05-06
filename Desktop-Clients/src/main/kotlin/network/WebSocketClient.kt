package network

import bssid.BssidFetcher
import bssid.BssidFetcherFactory
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.time.Instant

class WebSocketClient(private val email: String, private val bssid: String) {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    suspend fun connect() {
        try {
            println("Connected to bssid: $bssid")
            client.webSocket(
                urlString = "ws://4.213.5.84:8000/attendance/wifi/ws"
            ) {

                println("Connected to bssid: $bssid")
                println("email : $email")
                while (true) {
                    send(Frame.Text("""{"email": "$email", "bssid": "$bssid"}"""))
                    delay(5000)
                }
            }
        } catch (e: Exception) {
            println("❌ WebSocket Error: ${e.message}")
        }
    }
}
