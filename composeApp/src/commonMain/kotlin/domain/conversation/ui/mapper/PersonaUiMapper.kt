package domain.conversation.ui.mapper

import domain.conversation.entity.Persona
import domain.conversation.ui.model.PersonaUi

fun Persona.toUi() = PersonaUi(
    id = id,
    name = name,
    instructions = instructions,
)
