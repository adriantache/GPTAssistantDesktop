package new_structure.domain.conversation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import new_structure.data.ConversationRepositoryImpl
import new_structure.domain.conversation.data.ConversationRepository
import new_structure.domain.conversation.data.mapper.toData
import new_structure.domain.conversation.entity.Conversation
import new_structure.domain.conversation.entity.Message
import new_structure.domain.conversation.entity.Role
import new_structure.domain.conversation.event.ConversationEvent
import new_structure.domain.conversation.event.ConversationEvent.CopyToClipboard
import new_structure.domain.conversation.state.ConversationState
import new_structure.domain.conversation.state.ConversationState.Init
import new_structure.domain.conversation.state.ConversationState.OpenConversation
import new_structure.domain.conversation.ui.mapper.toUi
import new_structure.domain.util.model.Event

object ConversationUseCases {
    private val repository: ConversationRepository = ConversationRepositoryImpl()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private var conversation = Conversation()

    private val _state: MutableStateFlow<ConversationState> = MutableStateFlow(Init(::onInit))
    val state: StateFlow<ConversationState> = _state

    private val _event: MutableStateFlow<Event<ConversationEvent>?> = MutableStateFlow(null)
    val event: StateFlow<Event<ConversationEvent>?> = _event

    private fun onInit() {
        updateConversation()
    }

    private fun onMessageInput(input: String) {
        conversation = conversation.onMessageInput(input)
        updateConversation()
    }

    private fun onSubmitMessage() {
        conversation = conversation.onSubmit()

        scope.launch {
            var replyMessage = Message(content = "", role = Role.ASSISTANT)
            conversation = conversation.onUpdateMessage(replyMessage)
            updateConversation(isLoading = true)

            repository.getReplyStream(conversation.toData()).collect {
                replyMessage = replyMessage.copy(content = replyMessage.content + it)
                conversation = conversation.onUpdateMessage(replyMessage)
                updateConversation()
            }
        }
    }

    // TODO: navigate away instead?
    private fun onResetConversation() {
        conversation = Conversation()
        updateConversation()
    }

    private fun onCopyMessage(id: String) {
        val message = conversation.messages[id] ?: return
        _event.value = Event(CopyToClipboard(message.content))
    }

    private fun updateConversation(isLoading: Boolean = false) {
        updateState(
            OpenConversation(
                conversation = conversation.toUi(),
                isLoading = isLoading,
                onMessageInput = ::onMessageInput,
                onSubmitMessage = ::onSubmitMessage,
                onResetConversation = ::onResetConversation,
                onCopyMessage = ::onCopyMessage,
            )
        )
    }

    private fun updateState(newState: ConversationState) {
        synchronized(this) {
            _state.value = newState
        }
    }
}
