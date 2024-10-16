package new_structure.domain.conversationHistory.ui.mapper

import new_structure.domain.conversationHistory.entity.ConversationHistorySearchFilter
import new_structure.domain.conversationHistory.ui.model.ConversationHistorySearchFilterUi

fun ConversationHistorySearchFilter.toUi() = ConversationHistorySearchFilterUi(
    searchInput = searchInput,
    startDate = startDate,
    endDate = endDate,
)
