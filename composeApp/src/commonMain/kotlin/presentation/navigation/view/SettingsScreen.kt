package presentation.navigation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import domain.settings.state.SettingsState
import presentation.settings.tts.TtsVoiceSelectionView
import presentation.shared.CloseRow
import util.Strings.SETTINGS_CHANGE_TTS_VOICE_TEXT
import util.Strings.SETTINGS_CHANGE_TTS_VOICE_TITLE
import util.Strings.SETTINGS_DELETE_API_KEY
import util.Strings.SETTINGS_DELETE_API_KEY_TEXT
import util.Strings.SETTINGS_DELETE_API_KEY_TITLE

@Composable
fun SettingsScreen(
    settingsState: SettingsState.SettingsVisible,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CloseRow { settingsState.onDismiss() }

        SettingsRow(
            text = SETTINGS_DELETE_API_KEY,
            onClick = { settingsState.onClearApiKey() },
            confirmationTitle = SETTINGS_DELETE_API_KEY_TITLE,
            confirmationText = SETTINGS_DELETE_API_KEY_TEXT,
        )

        var displayTtsSelector by remember { mutableStateOf(false) }
        SettingsRow(
            onClick = { displayTtsSelector = true },
            title = SETTINGS_CHANGE_TTS_VOICE_TITLE,
            text = SETTINGS_CHANGE_TTS_VOICE_TEXT,
        )

        if (displayTtsSelector) {
            Dialog(onDismissRequest = { displayTtsSelector = false }) {
                TtsVoiceSelectionView(
                    modifier = Modifier.padding(16.dp),
                    onDismiss = { displayTtsSelector = false },
                )
            }
        }
    }
}
