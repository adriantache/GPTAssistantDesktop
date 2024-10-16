package new_structure.domain.conversation_history.event

sealed interface ConversationHistoryEvent {
    // TODO: reconsider this approach, maybe it makes more sense to create UI errors
    data class ErrorEvent(val errorMessage: String) : ConversationHistoryEvent
}
