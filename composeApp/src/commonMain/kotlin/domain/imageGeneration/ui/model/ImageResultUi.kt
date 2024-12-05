package domain.imageGeneration.ui.model

data class ImageResultUi(
    val imageUrl: String?,
    val imagePrompt: String,
    val errorMessage: String?,
)
