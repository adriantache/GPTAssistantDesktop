package new_structure.presentation.imageGeneration.model

data class ImageGenerationPromptItem(
    val input: String,
    val onInput: (String) -> Unit,
    val isLoading: Boolean,
    val canSubmit: Boolean,
    val onSubmit: () -> Unit,
)
