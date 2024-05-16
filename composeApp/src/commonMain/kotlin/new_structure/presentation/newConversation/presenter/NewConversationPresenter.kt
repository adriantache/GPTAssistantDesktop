package new_structure.presentation.newConversation.presenter

import new_structure.domain.conversation.state.ConversationState
import new_structure.domain.conversation.ui.model.RoleUi.ASSISTANT
import new_structure.domain.conversation.ui.model.RoleUi.USER
import new_structure.presentation.newConversation.model.MessageItem
import new_structure.presentation.newConversation.model.NewConversationItem
import new_structure.presentation.newConversation.model.RoleItem

class NewConversationPresenter {
    fun getNewConversationItem(state: ConversationState.OpenConversation): NewConversationItem {
        return NewConversationItem(
            messages = state.conversation.messages.map {
                MessageItem(
                    id = it.id,
                    message = it.content,
                    role = when (it.role) {
                        USER -> RoleItem.USER
                        ASSISTANT -> RoleItem.ASSISTANT
                    }
                )
            },
            input = state.conversation.input,
            onInput = state.onMessageInput,
            isLoading = state.isLoading,
            onSubmit = state.onSubmitMessage,
            canSubmit = state.conversation.canSubmit,
        )
    }
}
