package new_structure.domain.conversation.ui.mapper

import new_structure.domain.conversation.entity.Persona
import new_structure.domain.conversation.ui.model.PersonaUi

fun Persona.toUi() = PersonaUi(
    id = id,
    name = name,
    instructions = instructions,
)
