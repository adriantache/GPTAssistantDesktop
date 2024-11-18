package data.persona.mapper

import data.persona.model.PersonaDataItem
import domain.persona.data.model.PersonaData
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
