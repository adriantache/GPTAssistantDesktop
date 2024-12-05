package domain.imageGeneration.data

import domain.imageGeneration.data.model.ImageResultData

interface ImageGenerationRepository {
    suspend fun generateImage(prompt: String): ImageResultData
}
