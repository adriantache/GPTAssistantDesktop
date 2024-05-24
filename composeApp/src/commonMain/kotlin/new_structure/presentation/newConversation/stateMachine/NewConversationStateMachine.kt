package new_structure.presentation.newConversation.stateMachine

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import new_structure.domain.conversation.ConversationUseCases
import new_structure.domain.conversation.event.ConversationEvent.*
import new_structure.domain.conversation.state.ConversationState
import new_structure.presentation.newConversation.NewConversationScreen
import new_structure.presentation.newConversation.presenter.NewConversationPresenter
import new_structure.presentation.newConversation.view.AddPersonaDialog
import new_structure.presentation.newConversation.view.PersonaSelectorDialog

@Composable
fun NewConversationStateMachine(
    newConversationUseCases: ConversationUseCases = ConversationUseCases,
    presenter: NewConversationPresenter = NewConversationPresenter(),
) {
    val state by newConversationUseCases.state.collectAsState()
    val event by newConversationUseCases.event.collectAsState()
    // TODO: handle events

    LaunchedEffect(state) {
        when (val localState = state) {
            is ConversationState.Init -> localState.onInit()
            is ConversationState.OpenConversation -> Unit
        }
    }

    when (val localState = state) {
        is ConversationState.Init -> Unit
        is ConversationState.OpenConversation -> NewConversationScreen(presenter.getNewConversationItem(localState))
    }

    var showPersonasEvent: PersonaSelector? by remember { mutableStateOf(null) }
    var showAddPersonaEvent by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    event?.value?.let {
        when (it) {
            is CopyToClipboard -> clipboardManager.setText(AnnotatedString(it.contents))
            is PersonaSelector -> showPersonasEvent = it
            AddPersona -> showAddPersonaEvent = true
        }
    }

    showPersonasEvent?.let { localEvent ->
        PersonaSelectorDialog(
            personas = localEvent.personas.map { presenter.getPersonaItem(it, localEvent) },
            onAddPersona = localEvent.onAddPersona,
            onClearPersona = localEvent.onClearPersona,
            onDismiss = { showPersonasEvent = null },
        )
    }

    if (showAddPersonaEvent) {
        AddPersonaDialog { showAddPersonaEvent = false }
    }
}
