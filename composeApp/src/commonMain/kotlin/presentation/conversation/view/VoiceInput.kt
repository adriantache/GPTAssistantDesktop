package presentation.conversation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import domain.util.model.Event
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.baseline_mic_24
import org.jetbrains.compose.resources.painterResource
import platformSpecific.tts.AudioRecognizer
import platformSpecific.tts.model.RecognizerState
import theme.AppColor

@Composable
fun VoiceInput(
    onPromptChanged: (input: String) -> Unit,
    onSubmit: () -> Unit
) {
    var recognizerState: RecognizerState? by remember { mutableStateOf(null) }
    var startRecognizing by remember { mutableStateOf(Event(true)) }

    LaunchedEffect(recognizerState) {
        (recognizerState as? RecognizerState.Success)?.result?.value?.let {
            onPromptChanged(it)
            onSubmit()
            startRecognizing = Event(false)
        }

        if (recognizerState is RecognizerState.Failure) {
            startRecognizing = Event(false)
        }
    }

    AudioRecognizer(
        shouldRecognize = startRecognizing,
        onStateChange = { recognizerState = it },
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.requiredSize(100.dp)
                    .background(AppColor.accent(), CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        // Start recognizing if in any state other than Recognizing, otherwise stop it.
                        startRecognizing = Event(recognizerState !is RecognizerState.Recognizing)
                    },
                contentAlignment = Alignment.Center,
            ) {
                val localState = recognizerState

                if (localState is RecognizerState.Recognizing) {
                    MicrophoneInputDisplay(
                        modifier = Modifier.requiredSize(64.dp),
                        amplitudePercent = localState.amplitudePercent,
                        color = AppColor.onBackground(),
                    )
                } else {
                    Icon(
                        modifier = Modifier.requiredSize(24.dp),
                        painter = painterResource(Res.drawable.baseline_mic_24),
                        contentDescription = "Microphone input off"
                    )
                }
            }
        }
    }
}
