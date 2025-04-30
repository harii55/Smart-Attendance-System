package bssid

object BssidFetcherFactory {

    fun getBssidFetcher(): BssidFetcher{
        val os = System.getProperty("os.name")
        println("OS: $os")
        return when{
            os.contains("Windows", ignoreCase = true) -> WindowsBssidFetcher()
            os.contains("Mac", ignoreCase = true) -> MacosBssidFetcher()
            else -> LinuxBssidFetcher()
        }
    }
}