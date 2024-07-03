package new_structure.domain.imageGeneration.data

interface ImageGenerationRepository {
    suspend fun generateImage(prompt: String): String
}
