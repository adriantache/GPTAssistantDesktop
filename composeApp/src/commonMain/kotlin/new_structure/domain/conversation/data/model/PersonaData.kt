package new_structure.domain.conversation.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonaData(
    val id: String,
    val name: String,
    val instructions: String,
)
