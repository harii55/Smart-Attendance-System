package network

import bssid.BssidFetcher
import bssid.BssidFetcherFactory
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.time.Instant

class WebSocketClient(
    private val endpoint: String,
    private val email: String,
    private val bssid: String,
    private val onStatusChange: (String) -> Unit = {},
    private val onConnectionUpdate: (Int) -> Unit = {},
    private val onError: (String) -> Unit = {}
) {
    private val client = HttpClient(CIO) { install(WebSockets) }
    suspend fun connect() {
        try {
            client.webSocket(urlString = endpoint) {
                onStatusChange("Connected to attendance session. Chill now...")
                val basePayload = """{"email":"$email","bssid":"$bssid"}""";
                send(Frame.Text(basePayload))
                var startTime = System.currentTimeMillis()

                val timer = CoroutineScope(Dispatchers.IO).launch {
                    while (true) {
                        delay(1000)
                        val elapsed = (System.currentTimeMillis() - startTime) / 1000
                        onConnectionUpdate(elapsed.toInt())
                    }
                }

                val heartbeatJob = launch (Dispatchers.IO) {
                    while (isActive) {
                        delay(5000)
                        send(Frame.Text(basePayload))
                    }
                }

                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            println("➔ ${frame.readText()}")
                            onStatusChange(frame.readText())
                        }
                    }
                } finally {
                    heartbeatJob.cancelAndJoin()
                    timer.cancelAndJoin()
                }
            }
        } catch (e: CancellationException) {
            onError("Connection Cancelled")
        } catch (e: Exception) {
            onError("Connection Error: ${e.message}")
        }finally {
            client.close()
        }
    }
}


