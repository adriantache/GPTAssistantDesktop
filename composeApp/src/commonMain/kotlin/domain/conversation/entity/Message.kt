package domain.conversation.entity

import java.util.*

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val role: Role,
)
