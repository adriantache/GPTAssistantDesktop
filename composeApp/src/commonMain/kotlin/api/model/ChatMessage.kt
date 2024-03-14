package api.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val content: String,
    val role: ChatRole = ChatRole.user,
)

@Suppress("EnumEntryName")
enum class ChatRole {
    system, assistant, user
}
