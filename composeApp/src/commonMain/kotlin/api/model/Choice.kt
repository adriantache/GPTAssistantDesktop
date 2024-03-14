package api.model

import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val message: ChatMessage,
    val text: String?,
    val index: Int?,
    val logprobs: List<Double>?,
)
