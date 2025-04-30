package bssid

object BssidFetcherFactory {

    fun getBssidFetcher(): BssidFetcher{
        val os = System.getProperty("os.name")
        println("OS: $os")

        return when{
            os.contains("win") -> WindowsBssidFetcher()
            os.contains("mac") -> MacosBssidFetcher()
            else -> LinuxBssidFetcher()
        }
    }

}