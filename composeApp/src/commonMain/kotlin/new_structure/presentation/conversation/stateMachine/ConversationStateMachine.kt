package new_structure.presentation.conversation.stateMachine

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import new_structure.domain.conversation.ConversationUseCases
import new_structure.domain.conversation.event.ConversationEvent.*
import new_structure.domain.conversation.state.ConversationState
import new_structure.presentation.conversation.NewConversationScreen
import new_structure.presentation.conversation.presenter.ConversationPresenter
import new_structure.presentation.conversation.view.ErrorEventDialog
import new_structure.presentation.persona.view.PersonaSelectorStateMachine

@Composable
fun ConversationStateMachine(
    conversationId: String?,
    newConversationUseCases: ConversationUseCases = ConversationUseCases,
    presenter: ConversationPresenter = ConversationPresenter(),
) {
    val state by newConversationUseCases.state.collectAsState()
    val event by newConversationUseCases.event.collectAsState()

    var showPersonasEvent by remember { mutableStateOf(false) }
    var showErrorEvent: String? by remember { mutableStateOf(null) }
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(state) {
        when (val localState = state) {
            is ConversationState.Init -> localState.onInit(conversationId)
            is ConversationState.OpenConversation -> Unit
        }
    }

    when (val localState = state) {
        is ConversationState.Init -> Unit
        is ConversationState.OpenConversation -> NewConversationScreen(presenter.getNewConversationItem(localState))
    }

    event?.value?.let {
        when (it) {
            is CopyToClipboard -> clipboardManager.setText(AnnotatedString(it.contents))
            ShowPersonaSelector -> showPersonasEvent = true
            is ErrorEvent -> showErrorEvent = it.errorMessage
        }
    }

    if (showPersonasEvent) {
        PersonaSelectorStateMachine(onDismiss = { showPersonasEvent = false })
    }

    showErrorEvent?.let {
        ErrorEventDialog(
            errorMessage = it,
            onDismiss = { showErrorEvent = null },
        )
    }
}
