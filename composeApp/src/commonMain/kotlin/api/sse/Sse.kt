package api.sse

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

const val HEADER_LAST_EVENT_ID = "Last-Event-Id"

typealias Milliseconds = Long

typealias HeadersProvider = suspend (EventId?) -> Map<String, String>
typealias QueryParamsProvider = suspend (EventId?) -> Map<String, String>

class UnauthorizedError : Throwable()
class NotEventStreamError : Throwable()

fun HttpClient.readSse(
    url: String,
    requestBody: String,
    headersProvider: HeadersProvider = { it?.let { mapOf(HEADER_LAST_EVENT_ID to it) } ?: emptyMap() },
    queryParamsProvider: QueryParamsProvider = { emptyMap() },
    defaultReconnectDelayMillis: Milliseconds = 3000L
): Flow<SseEvent> {
    var reconnectDelay: Milliseconds = defaultReconnectDelayMillis
    var lastEventId: String? = null

    return flow {
        coroutineScope {
            var stop = false

            while (!stop && isActive) {
                val customHeaders = headersProvider(lastEventId)
                val queryParams = queryParamsProvider(lastEventId)

                prepareRequest(
                    url = url,
                    headers = customHeaders,
                    requestBody = requestBody,
                    queryParams = queryParams
                ).execute { response ->
                    if (!response.status.isSuccess()) {
                        throw UnauthorizedError()
                    }

                    if (!response.isEventStream()) {
                        throw NotEventStreamError()
                    }

                    response.bodyAsChannel().readSse(
                        onSseEvent = { sseEvent ->
                            lastEventId = sseEvent.id
                            emit(sseEvent)

                            if (sseEvent.data == "[DONE]") {
                                stop = true
                            }
                        },
                        onRetryChanged = {
                            reconnectDelay = it
                        }
                    )
                }

                delay(reconnectDelay)
            }
        }
    }
}

private suspend fun HttpClient.prepareRequest(
    url: String,
    headers: Map<String, String> = emptyMap(),
    requestBody: String,
    queryParams: Map<String, String> = emptyMap()
): HttpStatement {
    return preparePost(url) {
        headers {
            append(HttpHeaders.Accept, "text/event-stream")
            append(HttpHeaders.CacheControl, "no-cache")
            append(HttpHeaders.Connection, "keep-alive")
            append(HttpHeaders.AccessControlAllowOrigin, "*")
            headers.forEach { (key, value) -> append(key, value) }
        }

        queryParams.forEach { (key, value) -> addOrReplaceParameter(key, value) }

        setBody(requestBody)
    }
}

private fun HttpResponse.isEventStream(): Boolean {
    val contentType = contentType() ?: return false

    return contentType.contentType == "text" && contentType.contentSubtype == "event-stream"
}

private fun HttpRequestBuilder.addOrReplaceParameter(key: String, value: String?) {
    if (value == null) return

    url.parameters.remove(key)
    url.parameters.append(key, value)
}
