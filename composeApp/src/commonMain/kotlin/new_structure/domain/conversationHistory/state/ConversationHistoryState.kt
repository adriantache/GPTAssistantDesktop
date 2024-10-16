package new_structure.domain.conversationHistory.state

import new_structure.domain.conversationHistory.ui.model.ConversationHistorySearchFilterUi
import new_structure.domain.conversationHistory.ui.model.ConversationHistoryUi
import java.time.LocalDateTime

sealed interface ConversationHistoryState {
    data class Init(val onInit: () -> Unit) : ConversationHistoryState

    data class OpenConversationHistory(
        val conversations: List<ConversationHistoryUi>?,
        val onOpenConversation: (id: String) -> Unit,
        val onDeleteConversation: (id: String) -> Unit,
        val searchFilter: ConversationHistorySearchFilterUi,
        val onSearchInput: (input: String) -> Unit,
        val onSearchSubmit: () -> Unit,
        val onStartDateSelect: (LocalDateTime) -> Unit,
        val onEndDateSelect: (LocalDateTime) -> Unit,
        val onClearStartDate: () -> Unit,
        val onClearEndDate: () -> Unit,
    ) : ConversationHistoryState
}
