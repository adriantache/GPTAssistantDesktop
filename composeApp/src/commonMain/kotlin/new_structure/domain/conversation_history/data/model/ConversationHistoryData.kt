package new_structure.domain.conversation_history.data.model

import java.time.LocalDateTime

data class ConversationHistoryData(
    val id: String,
    val title: String,
    val date: LocalDateTime,
)
