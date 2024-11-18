package domain.imageGeneration.state

import domain.imageGeneration.ui.model.ImagePromptUi
import domain.imageGeneration.ui.model.ImageResultUi

sealed interface ImageGenerationState {
    data class Init(val onInit: () -> Unit) : ImageGenerationState

    data class Prompt(
        val prompt: ImagePromptUi,
        val isLoading: Boolean,
    ) : ImageGenerationState

    data object Loading : ImageGenerationState

    data class Result(
        val result: ImageResultUi,
        val onReset: () -> Unit,
    ) : ImageGenerationState
}
