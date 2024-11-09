package new_structure.presentation.conversation.model

data class ConversationItem(
    val messages: List<MessageItem>,
    val input: String,
    val onInput: (input: String) -> Unit,
    val isLoading: Boolean,
    val canSubmit: Boolean,
    val onSubmit: (isVoiceInput: Boolean) -> Unit,
    val selectedPersona: String,
    val onSelectPersona: () -> Unit,
    val isVoiceInput: Boolean,
)
