package domain.imageGeneration.data

interface ImageGenerationRepository {
    suspend fun generateImage(prompt: String): String
}
