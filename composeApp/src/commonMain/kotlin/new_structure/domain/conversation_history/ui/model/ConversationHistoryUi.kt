package new_structure.domain.conversation_history.ui.model

import java.time.LocalDateTime

data class ConversationHistoryUi(
    val id: String,
    val title: String,
    val date: LocalDateTime,
)
