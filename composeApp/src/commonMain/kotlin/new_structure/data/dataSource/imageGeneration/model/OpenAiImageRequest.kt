package new_structure.data.dataSource.imageGeneration.model

import kotlinx.serialization.Serializable

private const val MODEL_DALL_E_3 = "dall-e-3"

@Suppress("unused")
private const val DEFAULT_SIZE = "1024x1024"
private const val TALL_SIZE = "1024x1792"
private const val DEFAULT_QUALITY = "standard"

@Suppress("unused")
private const val HD_QUALITY = "hd"

@Serializable
data class OpenAiImageRequest(
    val prompt: String,
    val model: String = MODEL_DALL_E_3,
    val n: Int = 1,
    val size: String = DEFAULT_SIZE,
    val quality: String = DEFAULT_QUALITY,
)
