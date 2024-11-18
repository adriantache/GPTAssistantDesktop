package domain.conversation.ui.mapper

import domain.conversation.entity.Conversation
import domain.conversation.ui.model.ConversationUi

fun Conversation.toUi() = ConversationUi(
    id = id,
    title = title,
    messages = messages.values.map { it.toUi() },
    persona = persona?.toUi(),
    canResetConversation = canResetConversation,
    canSubmit = canSubmit,
    input = currentInput,
)
