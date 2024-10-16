package new_structure.domain.conversationHistory.data.mapper

import new_structure.domain.conversationHistory.data.model.ConversationHistoryData
import new_structure.domain.conversationHistory.entity.ConversationHistoryEntry

fun ConversationHistoryData.toEntity() = ConversationHistoryEntry(
    id = id,
    title = title,
    date = date,
)
