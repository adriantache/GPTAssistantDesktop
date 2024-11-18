package domain.conversation

import data.conversation.ConversationRepositoryImpl
import data.conversationHistory.ConversationHistoryRepositoryImpl
import domain.conversation.data.ConversationRepository
import domain.conversation.data.mapper.toData
import domain.conversation.data.mapper.toEntity
import domain.conversation.entity.Conversation
import domain.conversation.entity.Message
import domain.conversation.entity.Persona
import domain.conversation.entity.Role
import domain.conversation.event.ConversationEvent
import domain.conversation.event.ConversationEvent.CopyToClipboard
import domain.conversation.event.ConversationEvent.ShowPersonaSelector
import domain.conversation.state.ConversationState
import domain.conversation.state.ConversationState.Init
import domain.conversation.state.ConversationState.OpenConversation
import domain.conversation.ui.mapper.toUi
import domain.conversationHistory.data.ConversationHistoryRepository
import domain.util.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object ConversationUseCases {
    // TODO: replace with DI
    private val repository: ConversationRepository = ConversationRepositoryImpl()
    private val historyRepository: ConversationHistoryRepository = ConversationHistoryRepositoryImpl()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private var conversation = Conversation()

    private val _state: MutableStateFlow<ConversationState> = MutableStateFlow(Init(::onInit))
    val state: StateFlow<ConversationState> = _state

    private val _event: MutableStateFlow<Event<ConversationEvent>?> = MutableStateFlow(null)
    val event: StateFlow<Event<ConversationEvent>?> = _event

    private fun onInit(conversationId: String?) {
        setupConversation(conversationId)
    }

    private fun setupConversation(conversationId: String?) {
        scope.launch {
            if (conversationId != null) {
                val conversationData = historyRepository.getConversation(conversationId)
                conversation = conversationData.toEntity()
            }

            updateConversation()
        }
    }

    private fun onMessageInput(input: String) {
        conversation = conversation.onMessageInput(input)
        updateConversation()
    }

    private fun onSubmitMessage(isVoiceInput: Boolean = false) {
        conversation = conversation.onSubmit()

        scope.launch {
            var replyMessage = Message(content = "", role = Role.ASSISTANT)
            conversation = conversation.onUpdateMessage(replyMessage)
            updateConversation(isLoading = true)

            repository.getReplyStream(conversation.toData()).collect { outcome ->
                outcome
                    .onFailure {
                        _event.value = Event(ConversationEvent.ErrorEvent(it.message))
                    }
                    .onSuccess {
                        replyMessage = replyMessage.copy(content = replyMessage.content + it)
                        conversation = conversation.onUpdateMessage(replyMessage)
                        updateConversation()
                    }
            }

            // TODO: improve behaviour so TTS can start as soon as we have a full sentence. This probably requires
            //  two separate behaviours, one for text which outputs the stream as we receive it, and one for TTS which
            //  waits until we have a full sentence or line. Ideally we would wait for one extra line to see if we get
            //  a period in it, otherwise we send it.
            updateConversation(isVoiceInput = isVoiceInput)

            if (conversation.shouldGenerateTitle) {
                val title = repository.getTitle(conversation.toData())
                conversation = conversation.onSetTitle(title)
            }

            historyRepository.saveConversation(conversation.toData())
        }
    }

    private fun onResetConversation() {
        conversation = Conversation()
        updateConversation()
    }

    private fun onCopyMessage(id: String) {
        val message = conversation.messages[id] ?: return
        _event.value = Event(CopyToClipboard(message.content))
    }

    private fun onSelectPersona() {
        _event.value = Event(ShowPersonaSelector)
    }

    internal fun onSetPersona(persona: Persona?) {
        conversation = conversation.onSetPersona(persona)
        updateConversation()
    }

    private fun updateConversation(
        isLoading: Boolean = false,
        isVoiceInput: Boolean = false
    ) {
        updateState(
            OpenConversation(
                conversation = conversation.toUi(),
                isLoading = isLoading,
                isVoiceInput = isVoiceInput,
                onMessageInput = ::onMessageInput,
                onSubmitMessage = ::onSubmitMessage,
                onResetConversation = ::onResetConversation,
                onCopyMessage = ::onCopyMessage,
                onSelectPersona = ::onSelectPersona,
            )
        )
    }

    private fun updateState(newState: ConversationState) {
        synchronized(this) {
            _state.value = newState
        }
    }
}
