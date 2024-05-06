package new_structure.domain.conversation.data.model

data class ConversationData(
    val id: String,
    val messages: List<MessageData>,
)
