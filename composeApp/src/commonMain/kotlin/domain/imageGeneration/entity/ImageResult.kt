package domain.imageGeneration.entity

data class ImageResult(
    val imageUrl: String?,
    val imagePrompt: String,
    val errorMessage: String?,
)
