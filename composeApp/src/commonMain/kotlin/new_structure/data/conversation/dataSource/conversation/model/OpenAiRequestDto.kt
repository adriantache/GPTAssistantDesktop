package new_structure.data.conversation.dataSource.conversation.model

import kotlinx.serialization.Serializable

// Models without version always point to latest version.
private const val MODEL_GPT4_OMNI = "gpt-4o"

@Serializable
data class OpenAiRequestDto(
    val messages: List<ChatMessageDto>,
    val model: String = MODEL_GPT4_OMNI,
    val stream: Boolean? = null,
)
