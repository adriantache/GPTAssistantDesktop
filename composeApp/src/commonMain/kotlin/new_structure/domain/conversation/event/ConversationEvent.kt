package new_structure.domain.conversation.event

import new_structure.domain.conversation.ui.model.PersonaUi

sealed interface ConversationEvent {
    // TODO: find a way to move this to Platform layer instead
    data class CopyToClipboard(val contents: String) : ConversationEvent

    data class PersonaSelector(
        val personas: List<PersonaUi>,
        val onAddPersona: () -> Unit,
        val onClearPersona: () -> Unit,
        val onEditPersona: (id: String) -> Unit,
        val onSelectPersona: (id: String) -> Unit,
        val onDeletePersona: (id: String) -> Unit,
    ) : ConversationEvent

    data object AddPersona : ConversationEvent

    data class EditPersona(val persona: PersonaUi) : ConversationEvent

    // TODO: reconsider this approach, maybe it makes more sense to create UI errors
    data class ErrorEvent(val errorMessage: String) : ConversationEvent
}
