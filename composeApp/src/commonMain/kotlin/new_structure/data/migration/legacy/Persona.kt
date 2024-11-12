package new_structure.data.migration.legacy

import kotlinx.serialization.Serializable

@Serializable
data class Persona(
    val name: String,
    val instructions: String,
)
