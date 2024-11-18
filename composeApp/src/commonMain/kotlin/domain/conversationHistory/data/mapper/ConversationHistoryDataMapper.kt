package domain.conversationHistory.data.mapper

import domain.conversationHistory.data.model.ConversationHistoryData
import domain.conversationHistory.entity.ConversationHistoryEntry

fun ConversationHistoryData.toEntity() = ConversationHistoryEntry(
    id = id,
    title = title,
    date = date,
)
