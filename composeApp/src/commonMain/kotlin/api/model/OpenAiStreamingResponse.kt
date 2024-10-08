package api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiStreamingResponse(
    val id: String,
    val choices: List<StreamingChoice>,
    val model: String?,
)

@Serializable
data class StreamingChoice(
    val index: Int,
    val delta: StreamingDelta,
    @Suppress("PropertyName") val finish_reason: String?,
)

@Serializable
data class StreamingDelta(
    val content: String?,
)
