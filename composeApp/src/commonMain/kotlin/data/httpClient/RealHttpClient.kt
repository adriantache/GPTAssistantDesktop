package data.httpClient

import getPlatform
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*

private const val ENABLE_LOGGING: Boolean = false

val realHttpClient = HttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = 5 * 60 * 1000 // ChatGPT can be very slow to reply, even when streaming...
        socketTimeoutMillis = 5 * 60 * 1000 // ChatGPT can be very slow to reply, even when streaming...
    }

    if (ENABLE_LOGGING) {
        install(Logging) {
            logger = if (getPlatform().name.contains("Android")) {
                Logger.ANDROID
            } else {
                // This doesn't seem to work on desktop...
                Logger.DEFAULT
            }

            level = LogLevel.ALL
        }
    }

    install(SSE)
}
