package api.sse

inline fun parseSseLine(
    line: String?,
    onSseRawEvent: (SseRawEvent) -> (Unit)
) {
    val parts = line.takeIf { !it.isNullOrBlank() }?.split(":", limit = 2)
    val field = parts?.getOrNull(0)?.trim()
    val value = parts?.getOrNull(1)?.trim().orEmpty()
    onSseRawEvent(
        when (field) {
            null -> SseRawEvent.End
            "" -> SseRawEvent.Comment(value)
            "id" -> SseRawEvent.Id(value)
            "data" -> SseRawEvent.Data(value)
            "event" -> SseRawEvent.Event(value)
            "retry" -> value.toLongOrNull()?.takeIf { it > 0 }?.let { SseRawEvent.Retry(it) }
                ?: SseRawEvent.Error(SseRawError.InvalidReconnectionTime(value))

            else -> SseRawEvent.Error(SseRawError.InvalidField(field, value))
        }
    )
}

sealed interface SseRawEvent {
    data class Id(val value: EventId) : SseRawEvent
    data class Event(val value: EventType) : SseRawEvent
    data class Data(val value: EventData) : SseRawEvent
    data class Comment(val value: String) : SseRawEvent
    data class Retry(val value: Milliseconds) : SseRawEvent
    data object End : SseRawEvent
    data class Error(val error: SseRawError) : SseRawEvent
}

sealed class SseRawError : Throwable() {
    data class InvalidField(val field: String, val value: String?) : SseRawError()
    data class InvalidReconnectionTime(val value: String) : SseRawError()
}
