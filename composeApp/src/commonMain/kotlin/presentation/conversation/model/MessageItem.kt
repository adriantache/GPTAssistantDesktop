package presentation.conversation.model

data class MessageItem(
    val id: String,
    val message: String,
    val role: RoleItem,
)
