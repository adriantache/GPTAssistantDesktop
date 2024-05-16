package new_structure.domain.conversation.data.mapper

import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.entity.Conversation
import new_structure.domain.conversation.entity.Message
import new_structure.domain.conversation.entity.Role

fun Conversation.toData(): ConversationData {
    val messages = if (persona == null) {
        messages.values
    } else {
        val personaMessage = Message(
            content = persona.instructions,
            role = Role.USER,
        )

        listOf(personaMessage) + messages.values
    }

    return ConversationData(
        id = id,
        messages = messages.map { it.toData() },
    )
}
