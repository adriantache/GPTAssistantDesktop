package new_structure.domain.conversation_history.ui.model

import java.time.LocalDateTime

data class ConversationHistorySearchFilterUi(
    val searchInput: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
)
