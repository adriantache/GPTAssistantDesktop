package domain.persona.state

import domain.conversation.ui.model.PersonaUi
import domain.persona.ui.model.NewPersonaUi

sealed interface PersonaState {
    data class Init(val onInit: () -> Unit) : PersonaState

    data class PersonaSelector(
        val personas: List<PersonaUi>?,
        val onAddPersona: () -> Unit,
        val onClearPersona: () -> Unit,
        val onEditPersona: (id: String) -> Unit,
        val onSelectPersona: (id: String) -> Unit,
        val onDeletePersona: (id: String) -> Unit,
        val onDismiss: () -> Unit,
    ) : PersonaState

    data class AddPersona(
        val newPersona: NewPersonaUi,
        val isEdit: Boolean,
        val onUpdateName: (String) -> Unit,
        val onUpdateInstructions: (String) -> Unit,
        val onSubmit: () -> Unit,
        val onDismiss: () -> Unit,
    ) : PersonaState

    data class Dismiss(
        val afterDismiss: () -> Unit,
    ) : PersonaState
}
