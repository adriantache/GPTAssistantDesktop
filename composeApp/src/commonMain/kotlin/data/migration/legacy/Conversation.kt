package data.migration.legacy

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Conversation(
    val contents: List<ChatMessage> = emptyList(),
    val id: String = UUID.randomUUID().toString(),
) {
    // TODO: improve this by giving a special role to persona messages
    // If we have multiple user messages at the start, it means we have a persona set.
    val title = if (contents.size < 2) {
        ""
    } else if (contents[1].role == ChatRole.user) {
        contents[1].content
    } else {
        contents[0].content
    }
}
