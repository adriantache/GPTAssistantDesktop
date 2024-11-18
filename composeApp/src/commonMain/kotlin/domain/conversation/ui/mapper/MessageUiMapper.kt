package domain.conversation.ui.mapper

import domain.conversation.entity.Message
import domain.conversation.ui.model.MessageUi

fun Message.toUi(): MessageUi {
    return MessageUi(
        id = id,
        content = content,
        role = role.toUi(),
    )
}
