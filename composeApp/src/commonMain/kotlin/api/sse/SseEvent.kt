package api.sse

typealias EventType = String
typealias EventData = String
typealias EventId = String

data class SseEvent(
    val id: EventId? = null,
    val event: EventType? = null,
    val data: EventData = "",
)
