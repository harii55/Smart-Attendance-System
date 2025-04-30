package bssid

import java.io.BufferedReader

class LinuxBssidFetcher : BssidFetcher {
    override fun getBssid(): String? {
        return try {
            val process = ProcessBuilder("nmcli", "-t", "-f", "ACTIVE,BSSID", "dev", "wifi").start()
            val output = process.inputStream.bufferedReader().use(BufferedReader::readLines)

            val rawBssid = output
                .firstOrNull { it.startsWith("yes:") }
                ?.substringAfter("yes:")
                ?.replace("\\", "")

            val normalized = rawBssid
                ?.split(":")
                ?.joinToString("-") { it.uppercase() }
            normalized
        } catch (e: Exception) {
            println("❌ Failed to get BSSID: ${e.message}")
            null
        }
    }
}
