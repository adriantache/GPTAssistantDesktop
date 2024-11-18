package domain.conversation.data.mapper

import domain.conversation.entity.Persona
import domain.persona.data.model.PersonaData

fun Persona.toData() = PersonaData(
    id = id,
    name = name,
    instructions = instructions,
)

fun PersonaData.toEntity() = Persona(
    id = id,
    name = name,
    instructions = instructions,
)
