package new_structure.domain.conversation.data.model

data class ConversationData(
    val id: String,
    val messages: Map<String, MessageData>,
    // TODO: add persona and figure out impact of having a persona that doesn't exist in the repository anymore
)
