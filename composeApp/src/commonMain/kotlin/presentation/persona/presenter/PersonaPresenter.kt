package presentation.persona.presenter

import domain.conversation.ui.model.PersonaUi
import domain.persona.ui.model.NewPersonaUi
import presentation.persona.model.NewPersonaItem
import presentation.persona.model.PersonaItem

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
