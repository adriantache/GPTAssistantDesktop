package data.conversation.dataSource.model

import kotlinx.serialization.Serializable

// Models without version always point to latest version.
private const val MODEL_GPT4_OMNI = "04-mini"

@Serializable
data class OpenAiRequestDto(
    val messages: List<ChatMessageDto>,
    val model: String = MODEL_GPT4_OMNI,
    val stream: Boolean? = null,
)
