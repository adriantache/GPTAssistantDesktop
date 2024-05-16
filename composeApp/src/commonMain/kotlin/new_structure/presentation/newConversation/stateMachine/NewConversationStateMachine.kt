package new_structure.presentation.newConversation.stateMachine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import new_structure.domain.conversation.ConversationUseCases
import new_structure.domain.conversation.state.ConversationState
import new_structure.presentation.newConversation.NewConversationScreen
import new_structure.presentation.newConversation.presenter.NewConversationPresenter

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
}
