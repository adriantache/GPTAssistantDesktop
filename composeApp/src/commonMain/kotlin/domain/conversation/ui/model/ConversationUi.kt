package domain.conversation.ui.model

data class ConversationUi(
    val id: String,
    val title: String?,
    val messages: List<MessageUi>,
    val persona: PersonaUi?,
    val canResetConversation: Boolean,
    val canSubmit: Boolean,
    val input: String,
)
