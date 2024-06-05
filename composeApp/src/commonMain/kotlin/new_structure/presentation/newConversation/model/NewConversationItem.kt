package new_structure.presentation.newConversation.model

data class NewConversationItem(
    val messages: List<MessageItem>,
    val input: String,
    val onInput: (String) -> Unit,
    val isLoading: Boolean,
    val canSubmit: Boolean,
    val onSubmit: () -> Unit,
    val selectedPersona: String,
    val onSelectPersona: () -> Unit,
)
