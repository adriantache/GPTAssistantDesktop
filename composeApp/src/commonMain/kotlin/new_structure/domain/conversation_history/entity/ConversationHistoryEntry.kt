package new_structure.domain.conversation_history.entity

import java.time.LocalDateTime

data class ConversationHistoryEntry(
    val id: String,
    val title: String,
    val date: LocalDateTime,
)
