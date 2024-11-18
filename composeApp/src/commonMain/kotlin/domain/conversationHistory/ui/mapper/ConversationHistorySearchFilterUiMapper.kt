package domain.conversationHistory.ui.mapper

import domain.conversationHistory.entity.ConversationHistorySearchFilter
import domain.conversationHistory.ui.model.ConversationHistorySearchFilterUi

fun ConversationHistorySearchFilter.toUi() = ConversationHistorySearchFilterUi(
    searchInput = searchInput,
    startDate = startDate,
    endDate = endDate,
)
