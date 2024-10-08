package new_structure.data.dataSource.conversation.service

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*

val realHttpClient = HttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = 5 * 60 * 1000 // ChatGPT can be very slow to reply, even when streaming...
    }

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
    }
}
