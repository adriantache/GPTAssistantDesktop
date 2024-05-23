package new_structure.presentation.newConversation.presenter

import new_structure.domain.conversation.event.ConversationEvent
import new_structure.domain.conversation.state.ConversationState
import new_structure.domain.conversation.ui.model.PersonaUi
import new_structure.domain.conversation.ui.model.RoleUi.ASSISTANT
import new_structure.domain.conversation.ui.model.RoleUi.USER
import new_structure.presentation.newConversation.model.MessageItem
import new_structure.presentation.newConversation.model.NewConversationItem
import new_structure.presentation.newConversation.model.PersonaItem
import new_structure.presentation.newConversation.model.RoleItem
import new_structure.util.Strings.PERSONA_SELECTOR_NO_PERSONA_SELECTED

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
            selectedPersona = state.selectedPersona?.name ?: PERSONA_SELECTOR_NO_PERSONA_SELECTED,
            onSelectPersona = state.onSelectPersona,
        )
    }

    fun getPersonaItem(persona: PersonaUi, event: ConversationEvent.PersonaSelector): PersonaItem {
        return PersonaItem(
            id = persona.id,
            name = persona.name,
            instructions = persona.instructions,
            onSelect = { event.onSelectPersona(persona.id) },
            onDelete = { event.onDeletePersona(persona.id) },
        )
    }
}
