package new_structure.domain.conversation.ui.mapper

import new_structure.domain.conversation.entity.Message
import new_structure.domain.conversation.ui.model.MessageUi

fun Message.toUi(): MessageUi {
    return MessageUi(
        id = id,
        content = content,
        role = role.toUi(),
    )
}