package new_structure.presentation.conversation.model

data class ConversationItem(
    val messages: List<MessageItem>,
    val input: String,
    val onInput: (input: String, isVoiceInput: Boolean) -> Unit,
    val isLoading: Boolean,
    val canSubmit: Boolean,
    val onSubmit: () -> Unit,
    val selectedPersona: String,
    val onSelectPersona: () -> Unit,
    val isVoiceInput: Boolean,
)
