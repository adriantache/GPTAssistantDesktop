package domain.imageGeneration.ui.mapper

import domain.imageGeneration.entity.ImageResult
import domain.imageGeneration.ui.model.ImageResultUi

fun ImageResult.toUi() = ImageResultUi(
    image = image,
    imagePrompt = imagePrompt
)
