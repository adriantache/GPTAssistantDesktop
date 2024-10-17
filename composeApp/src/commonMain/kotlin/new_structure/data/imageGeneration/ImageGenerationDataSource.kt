package new_structure.data.imageGeneration

import new_structure.data.imageGeneration.service.ApiCaller
import new_structure.domain.imageGeneration.data.ImageGenerationRepository

// Note: Skipping repository here since there is no complex data processing.
class ImageGenerationDataSource(
    private val apiService: ApiCaller = ApiCaller(),
) : ImageGenerationRepository {
    override suspend fun generateImage(prompt: String): String {
        return apiService.getImage(prompt)
    }
}
