package new_structure.presentation.navigation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import new_structure.domain.settings.state.SettingsState
import new_structure.presentation.shared.CloseRow
import new_structure.util.Strings.SETTINGS_DELETE_API_KEY
import new_structure.util.Strings.SETTINGS_DELETE_API_KEY_TEXT
import new_structure.util.Strings.SETTINGS_DELETE_API_KEY_TITLE

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
