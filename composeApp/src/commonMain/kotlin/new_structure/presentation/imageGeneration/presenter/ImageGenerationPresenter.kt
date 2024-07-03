package new_structure.presentation.imageGeneration.presenter

import new_structure.domain.imageGeneration.state.ImageGenerationState
import new_structure.presentation.imageGeneration.model.ImageGenerationPromptItem
import new_structure.presentation.imageGeneration.model.ImageGenerationResultItem

class ImageGenerationPresenter {
    fun getPromptItem(localState: ImageGenerationState.Prompt): ImageGenerationPromptItem {
        return ImageGenerationPromptItem(
            input = localState.prompt.input,
            onInput = localState.prompt.onInput,
            canSubmit = localState.prompt.canSubmit,
            onSubmit = localState.prompt.onSubmit,
            isLoading = localState.isLoading,
        )
    }

    fun getResultItem(localState: ImageGenerationState.Result): ImageGenerationResultItem {
        return ImageGenerationResultItem(
            image = localState.result.image,
            imageInput = localState.result.imagePrompt,
            onReset = localState.onReset,
        )
    }
}
