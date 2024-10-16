package new_structure.domain.conversation_history.ui.mapper

import new_structure.domain.conversation_history.entity.ConversationHistorySearchFilter
import new_structure.domain.conversation_history.ui.model.ConversationHistorySearchFilterUi

fun ConversationHistorySearchFilter.toUi() = ConversationHistorySearchFilterUi(
    searchInput = searchInput,
    startDate = startDate,
    endDate = endDate,
)
