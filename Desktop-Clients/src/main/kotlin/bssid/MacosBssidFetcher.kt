package bssid

import java.io.BufferedReader

class MacosBssidFetcher : BssidFetcher {
    override fun getBssid(): String? {
        return tryWdutil() ?: tryAirport().also {
            if (it != null) println("ℹ️ Fallback: using airport -I")
        } ?: run {
            println("❌ Failed to fetch BSSID on macOS")
            null
        }
    }

    private fun tryWdutil(): String? = try {
        val process = ProcessBuilder("sudo", "/usr/bin/wdutil", "info").start()
        val output = process.inputStream.bufferedReader().use(BufferedReader::readLines)

        if (output.any { it.contains("usage:") || it.contains("redacted", ignoreCase = true) }) {
            println("⚠️ wdutil info did not return usable BSSID")
            null
        }

        val bssidLine = output.firstOrNull { it.trim().startsWith("BSSID") }
        val raw = bssidLine?.substringAfter(":")?.trim()
        val formatted = raw?.split(":")?.joinToString("-") { it.uppercase() }
        println("✅ BSSID via wdutil: $formatted")
        formatted
    } catch (e: Exception) {
        println("❌ wdutil failed: ${e.message}")
        null
    }

    private fun tryAirport(): String? = try {
        val process = ProcessBuilder(
            "/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport",
            "-I"
        ).start()
        val output = process.inputStream.bufferedReader().use(BufferedReader::readLines)
        println(output)
        val bssidLine = output.firstOrNull { it.trim().startsWith("BSSID") }
        val raw = bssidLine?.substringAfter(":")?.trim()
        val formatted = raw?.split(":")?.joinToString("-") { it.uppercase() }
        println("✅ BSSID via airport: $formatted")
        formatted
    } catch (e: Exception) {
        println("❌ airport failed: ${e.message}")
        null
    }
}