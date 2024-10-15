package old_code.api.model

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

    // TODO: improve this after adding the persona chat role
    val isEmpty = contents.size < 2

    fun add(message: ChatMessage): Conversation {
        return this.copy(contents = contents + message)
    }

    fun update(message: ChatMessage): Conversation {
        if (contents.none { it.id == message.id }) return this.copy(contents = contents + message)

        val newList = contents.toMutableList()
        newList.replaceAll {
            if (it.id == message.id) message else it
        }

        return this.copy(contents = newList)
    }
}
