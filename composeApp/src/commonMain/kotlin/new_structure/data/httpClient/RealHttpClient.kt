package new_structure.data.httpClient

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*

private const val ENABLE_LOGGING: Boolean = false

val realHttpClient = HttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = 5 * 60 * 1000 // ChatGPT can be very slow to reply, even when streaming...
    }

    if (ENABLE_LOGGING) {
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
    }

    install(SSE)
}
