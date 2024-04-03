package api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiResponse(
    val id: String,
    val choices: List<Choice>,
    val model: String?,
    val engine: String?,
    val prompt: String?,
    val temperature: Double?,
)

@Serializable
data class Choice(
    val message: ChatMessage,
    val text: String?,
    val index: Int?,
    val logprobs: List<Double>?,
)
