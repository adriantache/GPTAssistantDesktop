package domain.conversation.data.model

data class MessageData(
    val id: String,
    val content: String,
    val role: RoleData,
)
