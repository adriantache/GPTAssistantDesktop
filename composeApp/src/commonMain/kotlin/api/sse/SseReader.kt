package api.sse

import io.ktor.utils.io.*

suspend inline fun ByteReadChannel.readSse(
    onSseEvent: (SseEvent) -> (Unit),
    onRetryChanged: (Milliseconds) -> (Unit)
) {
    var id: EventId? = null
    var event: EventType? = null
    var data: EventData? = null

    while (!isClosedForRead) {
        parseSseLine(
            line = readUTF8Line(),
            onSseRawEvent = { sseRawEvent ->
                when (sseRawEvent) {
                    SseRawEvent.End -> {
                        if (data != null) {
                            onSseEvent(SseEvent(id, event, data!!))
                            id = null
                            event = null
                            data = null
                        } else {
                            // do nothing - maybe it's end after comment
                        }
                    }

                    is SseRawEvent.Id -> id = sseRawEvent.value
                    is SseRawEvent.Event -> event = sseRawEvent.value
                    is SseRawEvent.Data -> data = sseRawEvent.value
                    is SseRawEvent.Comment -> {
                        // do nothing
                    }

                    is SseRawEvent.Error -> {
                        // do nothing for now
                    }

                    is SseRawEvent.Retry -> onRetryChanged(sseRawEvent.value)
                }
            }
        )
    }
}
