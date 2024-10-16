package new_structure.presentation.conversationHistory

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            Text(
                modifier = Modifier.clickable {
                    localState.conversations?.firstOrNull()?.let { localState.onOpenConversation(it.id) }
                },
                text = state.toString()
            )
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
