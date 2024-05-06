package new_structure.domain.conversation.state

import new_structure.domain.conversation.ui.model.ConversationUi

sealed interface ConversationState {
    data class Init(val onInit: () -> Unit) : ConversationState

    data class OpenConversation(
        val conversation: ConversationUi,
        val onMessageInput: (String) -> Unit,
        val onSubmitMessage: () -> Unit,
        val onResetConversation: () -> Unit,
        val onCopyMessage: (id: String) -> Unit,
        /**
         * Consider other interactions
         * - edit previous message
         * - search Google
         * - split conversation?
         * - set title?
         * - set tags?
         */
    ) : ConversationState
}