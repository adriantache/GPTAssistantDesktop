package new_structure.domain.conversation.ui.mapper

import new_structure.domain.conversation.entity.Conversation
import new_structure.domain.conversation.ui.model.ConversationUi

fun Conversation.toUi() = ConversationUi(
    id = id,
    messages = messages.map { it.toUi() },
    persona = persona?.toUi(),
    canResetConversation = canResetConversation,
    canSubmit = canSubmit,
)