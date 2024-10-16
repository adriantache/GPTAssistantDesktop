package new_structure.domain.conversation.data.mapper

import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.entity.Conversation
import new_structure.domain.conversation.entity.Message
import new_structure.domain.conversation.entity.Role

fun Conversation.toData(): ConversationData {
    val messages = if (persona == null) {
        messages
    } else {
        val personaMessage = Message(
            content = persona.instructions,
            role = Role.USER,
        )

        mapOf(personaMessage.id to personaMessage).toMutableMap().apply {
            this.putAll(messages)
        }
    }

    return ConversationData(
        id = id,
        messages = messages.mapValues { it.value.toData() },
    )
}

fun ConversationData.toEntity(): Conversation {
    return Conversation(
        id = id,
        currentInput = "",
        messages = messages.mapValues { it.value.toEntity() },
        persona = null, // TODO: fix the persona retrieving, if desired
    )
}
