package platformSpecific.tts

import androidx.compose.runtime.Composable
import new_structure.domain.util.model.Event
import platformSpecific.tts.model.RecognizerState

@Composable
expect fun AudioRecognizer(
    shouldRecognize: Event<Boolean>,
    onStateChange: (RecognizerState) -> Unit,
    content: @Composable () -> Unit,
)
