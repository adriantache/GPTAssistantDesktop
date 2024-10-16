package new_structure.domain.conversationHistory.ui.mapper

import new_structure.domain.conversationHistory.entity.ConversationHistoryEntry
import new_structure.domain.conversationHistory.ui.model.ConversationHistoryUi

fun ConversationHistoryEntry.toUi() = ConversationHistoryUi(
    id = id,
    title = title,
    date = date,
)
