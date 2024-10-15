package old_code.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

// TODO: modify this to support vision input: https://platform.openai.com/docs/guides/vision
@Serializable
data class ChatMessage(
    val content: String,
    val role: ChatRole = ChatRole.user,

    @Transient // Important not to send this to ChatGpt.
    val id: String = UUID.randomUUID().toString(),
)

@Suppress("EnumEntryName", "unused")
enum class ChatRole {
    system, assistant, user
}
