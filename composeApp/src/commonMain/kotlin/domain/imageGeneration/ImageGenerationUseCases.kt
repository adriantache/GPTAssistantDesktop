package domain.imageGeneration

import data.imageGeneration.ImageGenerationDataSource
import domain.imageGeneration.data.ImageGenerationRepository
import domain.imageGeneration.data.model.ImageResultData.ImageResultError
import domain.imageGeneration.data.model.ImageResultData.ImageResultSuccess
import domain.imageGeneration.entity.ImagePrompt
import domain.imageGeneration.entity.ImageResult
import domain.imageGeneration.state.ImageGenerationState
import domain.imageGeneration.ui.mapper.toUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// TODO: [FEATURE] add history?
object ImageGenerationUseCases {
    // TODO: move to DI
    private val repository: ImageGenerationRepository = ImageGenerationDataSource()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state: MutableStateFlow<ImageGenerationState> = MutableStateFlow(ImageGenerationState.Init(::onInit))
    val state: StateFlow<ImageGenerationState> = _state

    private var imagePrompt = ImagePrompt()

    private fun onInit() {
        updatePrompt()
    }

    private fun onInput(input: String) {
        imagePrompt = imagePrompt.onInput(input)
        updatePrompt()
    }

    private fun onSubmit() {
        val prompt = imagePrompt.input

        imagePrompt = imagePrompt.onSubmit() ?: return

        _state.value = ImageGenerationState.Loading

        scope.launch {
            val imageResultData = repository.generateImage(prompt)

            val imageResult = ImageResult(
                imageUrl = (imageResultData as? ImageResultSuccess)?.imageUrl,
                imagePrompt = prompt,
                errorMessage = (imageResultData as? ImageResultError)?.errorMessage,
            )
            _state.value = ImageGenerationState.Result(
                result = imageResult.toUi(),
                onReset = ::onReset,
            )
        }
    }

    private fun onReset() {
        updatePrompt()
    }

    private fun updatePrompt(isLoading: Boolean = false) {
        _state.value = ImageGenerationState.Prompt(
            prompt = imagePrompt.toUi(
                onInput = ::onInput,
                onSubmit = ::onSubmit,
            ),
            isLoading = isLoading,
        )
    }
}
