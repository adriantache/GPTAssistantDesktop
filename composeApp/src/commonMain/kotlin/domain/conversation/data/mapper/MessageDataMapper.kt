package domain.conversation.data.mapper

import domain.conversation.data.model.MessageData
import domain.conversation.entity.Message

fun Message.toData() = MessageData(
    id = id,
    content = content,
    role = role.toData(),
)

fun MessageData.toEntity() = Message(
    id = id,
    content = content,
    role = role.toEntity()
)
