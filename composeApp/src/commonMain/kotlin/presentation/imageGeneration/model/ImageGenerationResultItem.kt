package presentation.imageGeneration.model

data class ImageGenerationResultItem(
    val image: String?,
    val imageInput: String,
    val onReset: () -> Unit,
    val errorMessage: String?,
)
