package api.model

import kotlinx.serialization.Serializable

// Models without version always point to latest version.
private const val MODEL_GPT_4_TURBO = "gpt-4-turbo-preview"

@Serializable
data class OpenAiRequest(
    val messages: List<ChatMessage>,
    val model: String = MODEL_GPT_4_TURBO,
    val stream: Boolean? = null,
)
