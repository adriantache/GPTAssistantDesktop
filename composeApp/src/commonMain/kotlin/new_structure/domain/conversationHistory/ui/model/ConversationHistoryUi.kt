package new_structure.domain.conversationHistory.ui.model

import java.time.LocalDateTime

data class ConversationHistoryUi(
    val id: String,
    val title: String,
    val date: LocalDateTime,
)
