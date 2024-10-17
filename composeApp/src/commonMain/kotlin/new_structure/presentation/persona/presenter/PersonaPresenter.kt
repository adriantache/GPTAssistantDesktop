package new_structure.presentation.persona.presenter

import new_structure.domain.conversation.ui.model.PersonaUi
import new_structure.domain.persona.ui.model.NewPersonaUi
import new_structure.presentation.persona.model.NewPersonaItem
import new_structure.presentation.persona.model.PersonaItem

class PersonaPresenter {
    fun getPersonaItem(persona: PersonaUi): PersonaItem = PersonaItem(
        id = persona.id,
        name = persona.name,
        instructions = persona.instructions
    )

    fun getNewPersonaItem(newPersonaUi: NewPersonaUi) = NewPersonaItem(
        name = newPersonaUi.name,
        instructions = newPersonaUi.instructions,
        canSubmit = newPersonaUi.canSubmit,
    )
}
