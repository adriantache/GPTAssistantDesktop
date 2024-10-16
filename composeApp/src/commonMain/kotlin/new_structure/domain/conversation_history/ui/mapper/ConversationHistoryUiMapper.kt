package new_structure.domain.conversation_history.ui.mapper

import new_structure.domain.conversation_history.entity.ConversationHistoryEntry
import new_structure.domain.conversation_history.ui.model.ConversationHistoryUi

fun ConversationHistoryEntry.toUi() = ConversationHistoryUi(
    id = id,
    title = title,
    date = date,
)
