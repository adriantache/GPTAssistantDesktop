package new_structure.data.persona.mapper

import new_structure.data.persona.model.PersonaDataItem
import new_structure.domain.persona.data.model.PersonaData
import java.time.Instant

fun PersonaData.toItem() = PersonaDataItem(
    id = id,
    createdAt = Instant.now().toEpochMilli(),
    name = name,
    instructions = instructions
)

fun PersonaDataItem.toData() = PersonaData(
    id = id,
    name = name,
    instructions = instructions
)
