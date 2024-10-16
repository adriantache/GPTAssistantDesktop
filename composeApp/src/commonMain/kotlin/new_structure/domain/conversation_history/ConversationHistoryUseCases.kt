package new_structure.domain.conversation_history

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import new_structure.data.conversation_history.ConversationHistoryRepositoryImpl
import new_structure.domain.conversation_history.data.ConversationHistoryRepository
import new_structure.domain.conversation_history.data.mapper.toEntity
import new_structure.domain.conversation_history.entity.ConversationHistoryEntry
import new_structure.domain.conversation_history.entity.ConversationHistorySearchFilter
import new_structure.domain.conversation_history.event.ConversationHistoryEvent
import new_structure.domain.conversation_history.state.ConversationHistoryState
import new_structure.domain.conversation_history.state.ConversationHistoryState.Init
import new_structure.domain.conversation_history.ui.mapper.toUi
import new_structure.domain.navigation.Navigator
import new_structure.domain.navigation.NavigatorImpl
import new_structure.domain.navigation.model.Destination
import new_structure.domain.util.model.Event
import java.time.LocalDateTime

object ConversationHistoryUseCases {
    // TODO: move to DI
    private val repository: ConversationHistoryRepository = ConversationHistoryRepositoryImpl()
    private val navigator: Navigator = NavigatorImpl
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private var conversations: List<ConversationHistoryEntry>? = null
    private var searchFilter: ConversationHistorySearchFilter = ConversationHistorySearchFilter()

    private val _state: MutableStateFlow<ConversationHistoryState> = MutableStateFlow(Init(::onInit))
    val state: StateFlow<ConversationHistoryState> = _state

    private val _event: MutableStateFlow<Event<ConversationHistoryEvent>?> = MutableStateFlow(null)
    val event: StateFlow<Event<ConversationHistoryEvent>?> = _event

    private fun onInit() {
        updateConversationHistory()
    }

    // TODO: test with a large amount of conversations, and maybe implement a custom paging solution
    private fun updateConversationHistory() {
        scope.launch {
            repository.getConversations(
                searchText = searchFilter.searchInput,
                startDateTime = searchFilter.startDate,
                endDateTime = searchFilter.endDate,
            ).onSuccess { conversationsData ->
                conversations = conversationsData.map { it.toEntity() }
                updateState()
            }.onFailure {
                _event.value = Event(ConversationHistoryEvent.ErrorEvent(it.message))
            }
        }
    }

    private fun onOpenConversation(id: String) {
        navigator.navigateTo(Destination.ConversationDestination(conversationId = id))
    }

    private fun onDeleteConversation(id: String) {
        scope.launch {
            repository.deleteConversation(id)

            updateConversationHistory()
        }
    }

    private fun onSearchInput(input: String) {
        searchFilter = searchFilter.onSearchInput(input)
    }

    private fun onStartDateSelect(date: LocalDateTime) {
        searchFilter = searchFilter.onStartDateSelect(date)
    }

    private fun onEndDateSelect(date: LocalDateTime) {
        searchFilter = searchFilter.onEndDateSelect(date)
    }

    private fun onClearStartDate() {
        searchFilter = searchFilter.onClearStartDate()
    }

    private fun onClearEndDate() {
        searchFilter = searchFilter.onClearEndDate()
    }

    private fun onSearchSubmit() {
        searchFilter = searchFilter.onSearchSubmit()

        if (!searchFilter.isValid) return

        updateConversationHistory()
    }

    private fun updateState() {
        _state.value = ConversationHistoryState.OpenConversationHistory(
            conversations = conversations?.map { it.toUi() },
            searchFilter = searchFilter.toUi(),
            onOpenConversation = ::onOpenConversation,
            onDeleteConversation = ::onDeleteConversation,
            onSearchInput = ::onSearchInput,
            onStartDateSelect = ::onStartDateSelect,
            onEndDateSelect = ::onEndDateSelect,
            onClearStartDate = ::onClearStartDate,
            onClearEndDate = ::onClearEndDate,
            onSearchSubmit = ::onSearchSubmit,
        )
    }
}
