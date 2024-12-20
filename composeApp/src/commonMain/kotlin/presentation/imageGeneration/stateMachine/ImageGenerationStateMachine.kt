package presentation.imageGeneration.stateMachine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import domain.imageGeneration.ImageGenerationUseCases
import domain.imageGeneration.state.ImageGenerationState.*
import presentation.imageGeneration.ImageGenerationPromptScreen
import presentation.imageGeneration.ImageGenerationResultScreen
import presentation.imageGeneration.presenter.ImageGenerationPresenter

@Composable
fun ImageGenerationStateMachine(
    imageGenerationUseCases: ImageGenerationUseCases = ImageGenerationUseCases,
    presenter: ImageGenerationPresenter = ImageGenerationPresenter(),
) {
    val state by imageGenerationUseCases.state.collectAsState()

    LaunchedEffect(state) {
        when (val localState = state) {
            is Init -> localState.onInit()
            is Prompt -> Unit
            is Result -> Unit
            Loading -> Unit
        }
    }

    when (val localState = state) {
        is Init -> Unit
        is Prompt -> ImageGenerationPromptScreen(presenter.getPromptItem(localState))
        is Result -> ImageGenerationResultScreen(presenter.getResultItem(localState))
        Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    }
}
