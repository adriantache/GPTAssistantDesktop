package presentation.conversationHistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.conversationHistory.ConversationHistoryUseCases
import domain.conversationHistory.event.ConversationHistoryEvent.ErrorEvent
import domain.conversationHistory.state.ConversationHistoryState
import presentation.conversation.view.ErrorEventDialog
import presentation.conversation.view.ScrollbarContainer
import presentation.conversationHistory.view.ConversationHistory

@Composable
fun ConversationHistoryStateMachine(
    conversationHistoryUseCases: ConversationHistoryUseCases = ConversationHistoryUseCases,
) {
    val state by conversationHistoryUseCases.state.collectAsState()
    val event by conversationHistoryUseCases.event.collectAsState()

    var showErrorEvent: String? by remember { mutableStateOf(null) }

    LaunchedEffect(state) {
        when (val localState = state) {
            is ConversationHistoryState.Init -> localState.onInit()
            is ConversationHistoryState.OpenConversationHistory -> Unit
        }
    }

    when (val localState = state) {
        is ConversationHistoryState.Init -> Unit
        is ConversationHistoryState.OpenConversationHistory -> {
            val listState = rememberLazyListState()

            ScrollbarContainer(listState) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = listState,
                ) {
                    localState.conversations?.let { conversations ->
                        items(items = conversations, key = { it.id }) {
                            ConversationHistory(
                                conversation = it,
                                onSelect = { localState.onOpenConversation(it.id) },
                                onDelete = { localState.onDeleteConversation(it.id) },
                            )
                        }
                    }
                }
            }
        }
    }

    event?.value?.let {
        when (it) {
            is ErrorEvent -> showErrorEvent = it.errorMessage
        }
    }

    showErrorEvent?.let {
        ErrorEventDialog(
            errorMessage = it,
            onDismiss = { showErrorEvent = null },
        )
    }
}
