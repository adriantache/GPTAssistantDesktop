package new_structure.domain.conversation.entity

import java.util.*

data class Persona(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val instructions: String,
)
