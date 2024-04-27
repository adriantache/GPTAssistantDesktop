package new_structure.domain.conversation.ui.model

data class ConversationUi(
    val id: String,
    val messages: List<MessageUi>,
    val persona: PersonaUi?,
    val canResetConversation: Boolean,
    val canSubmit: Boolean,
)
