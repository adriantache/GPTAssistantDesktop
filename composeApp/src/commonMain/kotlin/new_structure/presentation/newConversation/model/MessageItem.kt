package new_structure.presentation.newConversation.model

data class MessageItem(
    val id: String,
    val message: String,
    val role: RoleItem,
)
