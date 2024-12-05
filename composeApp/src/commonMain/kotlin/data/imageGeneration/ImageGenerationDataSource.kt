package data.imageGeneration

import data.imageGeneration.service.ApiCaller
import domain.imageGeneration.data.ImageGenerationRepository
import domain.imageGeneration.data.model.ImageResultData

// Note: Skipping repository here since there is no complex data processing.
class ImageGenerationDataSource(
    private val apiService: ApiCaller = ApiCaller(),
) : ImageGenerationRepository {
    override suspend fun generateImage(prompt: String): ImageResultData {
        return apiService.getImage(prompt)
    }
}
