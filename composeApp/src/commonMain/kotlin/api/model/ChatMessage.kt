package api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
data class ChatMessage(
    val content: String,
    val role: ChatRole = ChatRole.user,

    @Transient // Important not to send this to ChatGpt.
    val id: String = UUID.randomUUID().toString(),
)

@Suppress("EnumEntryName")
enum class ChatRole {
    system, assistant, user
}
