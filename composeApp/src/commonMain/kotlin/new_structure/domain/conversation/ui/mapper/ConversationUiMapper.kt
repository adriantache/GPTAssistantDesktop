package new_structure.domain.conversation.ui.mapper

import new_structure.domain.conversation.entity.Conversation
import new_structure.domain.conversation.ui.model.ConversationUi

fun Conversation.toUi() = ConversationUi(
    id = id,
    title = title,
    messages = messages.values.map { it.toUi() },
    persona = persona?.toUi(),
    canResetConversation = canResetConversation,
    canSubmit = canSubmit,
    input = currentInput,
)
