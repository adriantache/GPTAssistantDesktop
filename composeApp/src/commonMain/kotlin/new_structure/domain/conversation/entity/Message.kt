package new_structure.domain.conversation.entity

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val role: Role,
)
