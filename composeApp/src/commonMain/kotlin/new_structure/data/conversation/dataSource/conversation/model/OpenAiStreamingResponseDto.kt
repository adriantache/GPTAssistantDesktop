package new_structure.data.conversation.dataSource.conversation.model

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiStreamingResponseDto(
    val id: String,
    val choices: List<StreamingChoiceDto>,
    val model: String?,
)

@Serializable
data class StreamingChoiceDto(
    val index: Int,
    val delta: StreamingDeltaDto,
    @Suppress("PropertyName") val finish_reason: String?,
)

@Serializable
data class StreamingDeltaDto(
    val content: String?,
)
