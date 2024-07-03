package new_structure.domain.imageGeneration.ui.mapper

import new_structure.domain.imageGeneration.entity.ImageResult
import new_structure.domain.imageGeneration.ui.model.ImageResultUi

fun ImageResult.toUi() = ImageResultUi(
    image = image,
    imagePrompt = imagePrompt
)
