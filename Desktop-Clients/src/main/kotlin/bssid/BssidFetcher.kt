package bssid

interface BssidFetcher {
    fun getBssid(): String?
}