package new_structure.domain.conversation_history.data.mapper

import new_structure.domain.conversation_history.data.model.ConversationHistoryData
import new_structure.domain.conversation_history.entity.ConversationHistoryEntry

fun ConversationHistoryData.toEntity() = ConversationHistoryEntry(
    id = id,
    title = title,
    date = date,
)
