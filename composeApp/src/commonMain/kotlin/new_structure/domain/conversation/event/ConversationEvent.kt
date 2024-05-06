package new_structure.domain.conversation.event

sealed interface ConversationEvent {
    // TODO: find a way to move this to Platform layer instead
    data class CopyToClipboard(val contents: String): ConversationEvent
}