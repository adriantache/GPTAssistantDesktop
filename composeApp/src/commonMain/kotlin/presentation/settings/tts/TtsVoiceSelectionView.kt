package presentation.settings.tts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import platformSpecific.tts.getTtsHelper
import platformSpecific.tts.model.TtsVoice
import theme.AppColor

@Composable
fun TtsVoiceSelectionView(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    val ttsHelper by remember { lazy { getTtsHelper() } } // TODO: replace direct access with usecase
    var selectedTtsVoice: TtsVoice? by remember { mutableStateOf(null) }
    var voices: List<TtsVoice> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(ttsHelper) {
        ttsHelper?.let { ttsHelper ->
            voices = ttsHelper.getVoices()
            selectedTtsVoice = ttsHelper.getVoice()
        }
    }

    fun speak(voice: TtsVoice) {
        ttsHelper?.let { ttsHelper ->
            selectedTtsVoice = voice
            ttsHelper.setVoice(voice)
            ttsHelper.speak("Hello! This is ${voice.name} speaking.")
        } ?: onDismiss()
    }

    LazyColumn(modifier = modifier.fillMaxSize().background(AppColor.card())) {
        items(voices) { voice ->
            VoiceRow(
                voice = voice,
                selected = voice == selectedTtsVoice,
                onClick = { speak(it) },
            )
        }
    }
}

@Composable
fun VoiceRow(
    voice: TtsVoice,
    selected: Boolean,
    onClick: (TtsVoice) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(voice) })
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = voice.name ?: voice.id,
                    style = MaterialTheme.typography.subtitle1,
                    color = AppColor.onCard()
                )
                Text(
                    text = voice.id + " (${voice.quality})",
                    style = MaterialTheme.typography.caption,
                    color = AppColor.onCard()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.width(150.dp),
                text = voice.locale?.displayName.orEmpty(),
                style = MaterialTheme.typography.subtitle2,
                color = AppColor.onCard()
            )
        }
    }
    Divider()
}
