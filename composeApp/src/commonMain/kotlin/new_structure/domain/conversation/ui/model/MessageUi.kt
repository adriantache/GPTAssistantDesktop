package new_structure.domain.conversation.ui.model

data class MessageUi (
    val id: String,
    val content: String,
    val role: RoleUi,
)
