package new_structure.domain.conversation.data.mapper

import new_structure.domain.conversation.data.model.PersonaData
import new_structure.domain.conversation.entity.Persona

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
