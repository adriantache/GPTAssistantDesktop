package domain.conversation.data.mapper

import domain.conversation.data.model.ConversationData
import domain.conversation.entity.Conversation

fun Conversation.toData(): ConversationData {
    return ConversationData(
        id = id,
        title = title,
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
