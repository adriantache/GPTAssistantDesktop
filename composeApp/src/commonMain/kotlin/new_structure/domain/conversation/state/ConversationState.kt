package new_structure.domain.conversation.state

import new_structure.domain.conversation.ui.model.ConversationUi

sealed interface ConversationState {
    data class Init(val onInit: (conversationId: String?) -> Unit) : ConversationState

    data class OpenConversation(
        val conversation: ConversationUi,
        val isLoading: Boolean,
        val isVoiceInput: Boolean,
        // TODO: move isVoiceInput to onSubmit instead
        val onMessageInput: (input: String, isVoiceInput: Boolean) -> Unit,
        val onSubmitMessage: () -> Unit,
        val onResetConversation: () -> Unit,
        val onCopyMessage: (id: String) -> Unit,
        val onSelectPersona: () -> Unit,

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
