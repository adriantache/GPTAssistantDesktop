package presentation.conversation.presenter

import domain.conversation.state.ConversationState
import domain.conversation.ui.model.RoleUi
import domain.conversation.ui.model.RoleUi.ASSISTANT
import domain.conversation.ui.model.RoleUi.USER
import presentation.conversation.model.ConversationItem
import presentation.conversation.model.MessageItem
import presentation.conversation.model.RoleItem
import util.Strings.PERSONA_SELECTOR_NO_PERSONA_SELECTED

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
            isVoiceInput = state.isVoiceInput,
            onSubmit = state.onSubmitMessage,
            canSubmit = state.conversation.canSubmit,
            selectedPersona = state.conversation.persona?.name ?: PERSONA_SELECTOR_NO_PERSONA_SELECTED,
            onSelectPersona = state.onSelectPersona,
            onResetConversation = state.onResetConversation,
            hasMessages = state.conversation.messages.isNotEmpty(),
        )
    }
}
