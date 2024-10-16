package new_structure.domain.conversation.data.mapper

import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.entity.Message

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
