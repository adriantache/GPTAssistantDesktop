package presentation.navigation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.settings.state.SettingsState
import presentation.shared.CloseRow
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
    }
}
