package domain.conversation.event

sealed interface ConversationEvent {
    // TODO: find a way to move this to Platform layer instead
    data class CopyToClipboard(val contents: String) : ConversationEvent

    data object ShowPersonaSelector : ConversationEvent

    // TODO: reconsider this approach, maybe it makes more sense to create UI errors
    data class ErrorEvent(val errorMessage: String) : ConversationEvent
}
