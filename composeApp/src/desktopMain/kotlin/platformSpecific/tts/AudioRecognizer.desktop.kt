package platformSpecific.tts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import new_structure.domain.util.model.Event
import platformSpecific.tts.model.RecognizerState

@Composable
actual fun AudioRecognizer(
    shouldRecognize: Event<Boolean>,
    onStateChange: (RecognizerState) -> Unit,
    content: @Composable () -> Unit,
) {
    Text(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        text = "Audio input is not supported on this platform.",
        textAlign = TextAlign.Center,
    )
}
