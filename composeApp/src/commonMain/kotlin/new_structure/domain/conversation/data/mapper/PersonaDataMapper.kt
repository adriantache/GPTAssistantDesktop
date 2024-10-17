package new_structure.domain.conversation.data.mapper

import new_structure.domain.conversation.entity.Persona
import new_structure.domain.persona.data.model.PersonaData

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
