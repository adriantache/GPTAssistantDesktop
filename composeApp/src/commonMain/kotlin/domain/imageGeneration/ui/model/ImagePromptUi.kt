package domain.imageGeneration.ui.model

data class ImagePromptUi(
    val input: String,
    val onInput: (String) -> Unit,
    val canSubmit: Boolean,
    val onSubmit: () -> Unit,
)
