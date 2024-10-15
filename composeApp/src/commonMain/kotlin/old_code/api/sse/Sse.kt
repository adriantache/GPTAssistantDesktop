package old_code.api.sse

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import new_structure.data.dataSource.conversation.model.OpenAiError
import new_structure.domain.util.model.Outcome

fun HttpClient.readSse(
    url: String,
    requestBody: String,
    headers: Map<String, String>,
): Flow<Outcome<String>> {
    return flow {
        val request = prepareRequest(
            url = url,
            requestBody = requestBody,
            headers = headers,
        )

        request.execute { response ->
            if (!response.status.isSuccess()) {
                emit(Outcome.Failure(OpenAiError.HttpError(code = response.status.value, body = response.bodyAsText())))
                return@execute
            }

            val isEventStream = response.contentType()?.let {
                it.contentType == "text" && it.contentSubtype == "event-stream"
            }
            if (isEventStream != true) {
                emit(Outcome.Failure(OpenAiError.NotAnEventStreamError))
                return@execute
            }

            with(response.bodyAsChannel()) {
                // For whatever reason, these events need to end with an empty line, otherwise they're all empty. The
                //  empty line gets converted to a null, which flushes the buffer of cached data to an event.
                var cachedData: String? = null

                while (!this.isClosedForRead) {
                    val line = this.readUTF8Line()
                    val data = line?.getData()

                    when {
                        data == "[DONE]" -> return@execute
                        data != null -> cachedData = data
                        else -> cachedData?.let { emit(Outcome.Success(it)) }
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
