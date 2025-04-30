package network

import bssid.BssidFetcher
import bssid.BssidFetcherFactory
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

            val bssid = BssidFetcherFactory.getBssidFetcher().getBssid()
            println("Connected to bssid: $bssid")
            client.webSocket(
                urlString = "ws://localhost:8000/attendance/wifi/ws"
            ) {
                println("✅ WebSocket Connected at ${Instant.now()}")

                // Send presence every 5 seconds
                while (true) {
                    send(Frame.Text("""{"email": "hariny.24bcs10407@sst.scaler.com", "bssid": "$bssid"}"""))
                    delay(5000)
                }
            }
        } catch (e: Exception) {
            println("❌ WebSocket Error: ${e.message}")
        }
    }
}
