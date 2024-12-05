package domain.imageGeneration.data.model

sealed interface ImageResultData {
    data class ImageResultSuccess(val imageUrl: String) : ImageResultData
    data class ImageResultError(val errorMessage: String) : ImageResultData
}
