package new_structure.data.sse

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun HttpClient.readSse(
    url: String,
    requestBody: String,
    headers: Map<String, String>,
): Flow<String> {
    return flow {
        val request = prepareRequest(
            url = url,
            requestBody = requestBody,
            headers = headers,
        )

        request.execute { response ->
            // TODO: better error handling
            if (!response.status.isSuccess()) {
                error(response.bodyAsText())
            }

            val isEventStream = response.contentType()?.let {
                it.contentType == "text" && it.contentSubtype == "event-stream"
            }
            if (isEventStream != true) {
                error("Not an event stream!")
            }

            response.bodyAsChannel().let { channel ->
                // For whatever reason, these events need to end with an empty line, otherwise they're all empty.
                var cachedData: String? = null

                while (!channel.isClosedForRead) {
                    val line = channel.readUTF8Line()
                    val data = line?.getData()

                    when {
                        data == "[DONE]" -> return@execute
                        data != null -> cachedData = data
                        else -> cachedData?.let { emit(cachedData) }
                    }
                }
            }
        }
    }
}

private suspend fun HttpClient.prepareRequest(
    url: String,
    requestBody: String,
    headers: Map<String, String>,
): HttpStatement {
    return preparePost(url) {
        headers {
            append(HttpHeaders.Accept, "text/event-stream")
            append(HttpHeaders.CacheControl, "no-cache")
            append(HttpHeaders.Connection, "keep-alive")
            append(HttpHeaders.AccessControlAllowOrigin, "*")
            headers.forEach { (key, value) -> append(key, value) }
        }

        setBody(requestBody)
    }
}

private fun String.getData(): String? {
    val parts = this.split(":", limit = 2).takeIf { it.size == 2 } ?: return null
    val (type, data) = parts

    return data.trim().takeIf { type == "data" }
}
