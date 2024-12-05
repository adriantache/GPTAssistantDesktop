package data.imageGeneration.model

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiImageResponse(
    val created: Long?,
    val data: List<ImageData>?,
    val error: ImageError?,
) {
    val url = data?.first()?.url
}

@Serializable
data class ImageData(
    val url: String,
)

@Serializable
data class ImageError(
    val code: String,
    val message: String,
    val type: String,
)
