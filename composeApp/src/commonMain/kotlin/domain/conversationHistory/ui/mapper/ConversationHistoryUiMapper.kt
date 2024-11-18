package domain.conversationHistory.ui.mapper

import domain.conversationHistory.entity.ConversationHistoryEntry
import domain.conversationHistory.ui.model.ConversationHistoryUi

fun ConversationHistoryEntry.toUi() = ConversationHistoryUi(
    id = id,
    title = title,
    date = date,
)
