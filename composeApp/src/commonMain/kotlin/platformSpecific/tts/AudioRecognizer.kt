package platformSpecific.tts

import androidx.compose.runtime.Composable
import platformSpecific.tts.model.RecognizerState

@Composable
expect fun AudioRecognizer(
    shouldRecognize: Boolean,
    onStateChange: (RecognizerState) -> Unit,
    content: @Composable () -> Unit,
)
