package new_structure.presentation.conversation.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import platformSpecific.tts.AudioRecognizer
import platformSpecific.tts.model.RecognizerState
import theme.AppColor

@Composable
fun VoiceInput(
    onPromptChanged: (input: String) -> Unit,
    onSubmit: () -> Unit
) {
    var recognizerState: RecognizerState? by remember { mutableStateOf(null) }
    var startRecognizing by remember { mutableStateOf(false) }

    LaunchedEffect(recognizerState) {
        (recognizerState as? RecognizerState.Success)?.result?.value?.let {
            onPromptChanged(it)
            onSubmit()
            startRecognizing = false
        }

        if (recognizerState is RecognizerState.Failure) {
            startRecognizing = false
        }
    }

    AudioRecognizer(
        shouldRecognize = startRecognizing,
        onStateChange = {
            recognizerState = it
        },
    ) {
        TextButton(
            modifier = Modifier.padding(start = 8.dp),
            onClick = { startRecognizing = !startRecognizing },
        ) {
            Text(
                text = "Voice",
                style = MaterialTheme.typography.subtitle1,
                color = AppColor.userMessage(),
            )
        }
    }
}
