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
    val ttsHelper = remember { getTtsHelper() }
    var selectedTtsVoice by remember { mutableStateOf(ttsHelper?.getVoice()) }
    val voices = remember { ttsHelper?.getVoices().orEmpty() }

    LaunchedEffect(selectedTtsVoice) {
        ttsHelper?.let { ttsHelper ->
            selectedTtsVoice?.let {
                ttsHelper.setVoice(it)
                ttsHelper.speak("Hello! This is ${it.name} speaking.")
            }
        } ?: onDismiss()
    }

    LazyColumn(modifier = modifier.fillMaxSize().background(AppColor.card())) {
        items(voices) { voice ->
            VoiceRow(
                voice = voice,
                selected = voice == selectedTtsVoice,
                onClick = { selectedTtsVoice = it },
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
        Row {
            RadioButton(
                selected = selected,
                onClick = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = voice.name, style = MaterialTheme.typography.subtitle1, color = AppColor.onCard())
                Text(text = voice.name, style = MaterialTheme.typography.caption, color = AppColor.onCard())
            }
        }
    }
    Divider()
}
