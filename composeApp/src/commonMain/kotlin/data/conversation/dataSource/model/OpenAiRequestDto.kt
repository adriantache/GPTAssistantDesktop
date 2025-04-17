package data.conversation.dataSource.model

import kotlinx.serialization.Serializable

// Models without version always point to latest version.
private const val DEFAULT_MODEL = "o4-mini"

@Serializable
data class OpenAiRequestDto(
    val messages: List<ChatMessageDto>,
    val model: String = DEFAULT_MODEL,
    val stream: Boolean? = null,
)
