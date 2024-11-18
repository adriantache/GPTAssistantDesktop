package domain.conversationHistory.entity

import java.time.LocalDateTime

data class ConversationHistoryEntry(
    val id: String,
    val title: String?,
    val date: LocalDateTime,
)
