package bssid

import java.io.BufferedReader

class MacosBssidFetcher : BssidFetcher {
    override fun getBssid(): String? {
        return try {
            val process = ProcessBuilder("/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport", "-I").start()
            val output = process.inputStream.bufferedReader().use(BufferedReader::readLines)

            val bssidLine = output.firstOrNull { it.trim().startsWith("BSSID") }
            val rawBssid = bssidLine?.substringAfter(":")?.trim()
            val normalized = rawBssid
                ?.split(":")
                ?.joinToString("-") { it.uppercase() }

            normalized
        } catch (e: Exception) {
            println("❌ macOS BSSID fetch failed: ${e.message}")
            null
        }
    }
}