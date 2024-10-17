package new_structure.presentation.conversation.presenter

import new_structure.domain.conversation.state.ConversationState
import new_structure.domain.conversation.ui.model.RoleUi
import new_structure.domain.conversation.ui.model.RoleUi.ASSISTANT
import new_structure.domain.conversation.ui.model.RoleUi.USER
import new_structure.presentation.conversation.model.ConversationItem
import new_structure.presentation.conversation.model.MessageItem
import new_structure.presentation.conversation.model.RoleItem
import new_structure.util.Strings.PERSONA_SELECTOR_NO_PERSONA_SELECTED

class ConversationPresenter {
    fun getNewConversationItem(state: ConversationState.OpenConversation): ConversationItem {
        return ConversationItem(
            messages = state.conversation.messages.map {
                MessageItem(
                    id = it.id,
                    message = it.content,
                    role = when (it.role) {
                        USER -> RoleItem.USER
                        ASSISTANT -> RoleItem.ASSISTANT
                        RoleUi.SYSTEM -> RoleItem.SYSTEM
                    }
                )
            },
            input = state.conversation.input,
            onInput = state.onMessageInput,
            isLoading = state.isLoading,
            onSubmit = state.onSubmitMessage,
            canSubmit = state.conversation.canSubmit,
            selectedPersona = state.conversation.persona?.name ?: PERSONA_SELECTOR_NO_PERSONA_SELECTED,
            onSelectPersona = state.onSelectPersona,
        )
    }
}
