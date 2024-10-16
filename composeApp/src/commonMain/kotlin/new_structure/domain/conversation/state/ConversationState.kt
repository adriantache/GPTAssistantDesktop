package new_structure.domain.conversation.state

import new_structure.domain.conversation.ui.model.ConversationUi
import new_structure.domain.conversation.ui.model.PersonaUi

sealed interface ConversationState {
    data class Init(val onInit: (conversationId: String?) -> Unit) : ConversationState

    data class OpenConversation(
        val conversation: ConversationUi,
        val isLoading: Boolean,
        val onMessageInput: (String) -> Unit,
        val onSubmitMessage: () -> Unit,
        val onResetConversation: () -> Unit,
        val selectedPersona: PersonaUi?,
        val onSelectPersona: () -> Unit,
        val onCopyMessage: (id: String) -> Unit,

        /**
         * TODO Consider other interactions
         * - edit previous message
         * - search Google
         * - split conversation?
         * - set title?
         * - set tags?
         */
    ) : ConversationState
}
