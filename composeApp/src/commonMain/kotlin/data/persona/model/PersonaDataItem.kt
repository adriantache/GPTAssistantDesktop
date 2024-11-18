package data.persona.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonaDataItem(
    val id: String,
    val createdAt: Long,
    val name: String,
    val instructions: String,
)
