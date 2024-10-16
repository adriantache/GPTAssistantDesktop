package new_structure.presentation.imageGeneration

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import new_structure.presentation.conversation.view.PromptInput
import new_structure.presentation.imageGeneration.model.ImageGenerationPromptItem
import new_structure.util.Strings.IMAGE_INPUT_TUTORIAL

@Composable
fun ImageGenerationPromptScreen(
    imageGenerationItem: ImageGenerationPromptItem,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(IMAGE_INPUT_TUTORIAL)

        Spacer(Modifier.height(16.dp))

        Spacer(Modifier.weight(1f))

        PromptInput(
            prompt = imageGenerationItem.input,
            onPromptChanged = imageGenerationItem.onInput,
            isLoading = imageGenerationItem.isLoading,
            onSubmit = imageGenerationItem.onSubmit,
        )
    }
}
