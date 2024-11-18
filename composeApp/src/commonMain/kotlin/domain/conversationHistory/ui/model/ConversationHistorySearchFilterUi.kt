package domain.conversationHistory.ui.model

import java.time.LocalDateTime

data class ConversationHistorySearchFilterUi(
    val searchInput: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
)
