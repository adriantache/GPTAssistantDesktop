package api.model

import kotlinx.serialization.Serializable

@Serializable
data class Persona(
    val name: String,
    val instructions: String,
)
