package domain.conversation.state

import domain.conversation.ui.model.ConversationUi

sealed interface ConversationState {
    data class Init(val onInit: (conversationId: String?) -> Unit) : ConversationState

    data class OpenConversation(
        val conversation: ConversationUi,
        val isLoading: Boolean,
        val isVoiceInput: Boolean,
        val hasVoiceInput: Boolean,
        val onMessageInput: (input: String) -> Unit,
        val onSubmitMessage: (isVoiceInput: Boolean) -> Unit,
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
