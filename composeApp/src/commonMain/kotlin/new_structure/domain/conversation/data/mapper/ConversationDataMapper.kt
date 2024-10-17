package new_structure.domain.conversation.data.mapper

import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.entity.Conversation

fun Conversation.toData(): ConversationData {
    return ConversationData(
        id = id,
        messages = messages.mapValues { it.value.toData() },
        persona = persona?.toData(),
    )
}

fun ConversationData.toEntity(): Conversation {
    return Conversation(
        id = id,
        currentInput = "",
        messages = messages.mapValues { it.value.toEntity() },
        persona = persona?.toEntity(),
    )
}
