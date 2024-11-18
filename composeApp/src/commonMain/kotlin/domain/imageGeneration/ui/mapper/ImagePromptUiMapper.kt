package domain.imageGeneration.ui.mapper

import domain.imageGeneration.entity.ImagePrompt
import domain.imageGeneration.ui.model.ImagePromptUi

fun ImagePrompt.toUi(
    onInput: (String) -> Unit,
    onSubmit: () -> Unit
) = ImagePromptUi(
    input = input,
    onInput = onInput,
    canSubmit = canSubmit,
    onSubmit = onSubmit,
)
