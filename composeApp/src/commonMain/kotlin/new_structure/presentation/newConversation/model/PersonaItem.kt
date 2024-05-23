package new_structure.presentation.newConversation.model

data class PersonaItem(
    val id: String,
    val name: String,
    val instructions: String,
    val onSelect: () -> Unit,
    val onDelete: () -> Unit,
)
