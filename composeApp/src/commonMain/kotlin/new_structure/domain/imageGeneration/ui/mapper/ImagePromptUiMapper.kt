package new_structure.domain.imageGeneration.ui.mapper

import new_structure.domain.imageGeneration.entity.ImagePrompt
import new_structure.domain.imageGeneration.ui.model.ImagePromptUi

fun ImagePrompt.toUi(
    onInput: (String) -> Unit,
    onSubmit: () -> Unit
) = ImagePromptUi(
    input = input,
    onInput = onInput,
    canSubmit = canSubmit,
    onSubmit = onSubmit,
)
