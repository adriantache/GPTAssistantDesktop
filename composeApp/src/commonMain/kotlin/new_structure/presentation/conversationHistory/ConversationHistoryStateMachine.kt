package new_structure.presentation.conversationHistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import new_structure.domain.conversationHistory.ConversationHistoryUseCases
import new_structure.domain.conversationHistory.event.ConversationHistoryEvent.ErrorEvent
import new_structure.domain.conversationHistory.state.ConversationHistoryState
import new_structure.presentation.conversation.view.ErrorEventDialog

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
        is ConversationHistoryState.OpenConversationHistory ->
            // TODO: replace this test code with an actual implementation
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                localState.conversations?.let { conversations ->
                    items(items = conversations, key = { it.id }) {
                        Text(
                            modifier = Modifier.clickable { localState.onOpenConversation(it.id) }.fillMaxWidth(),
                            text = it.title ?: it.date.toString(),
                        )
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
