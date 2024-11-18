package domain.settings.state

sealed interface SettingsState {
    data class Init(val onInit: () -> Unit) : SettingsState

    data class MissingApiKey(
        val apiKey: String,
        val onInput: (String) -> Unit,
        val onSubmit: () -> Unit,
        val canSubmit: Boolean,
    ) : SettingsState

    data class Idle(
        val onDisplaySettings: () -> Unit,
    ) : SettingsState

    data class SettingsVisible(
        val onClearApiKey: () -> Unit,
        val onDismiss: () -> Unit,
    ) : SettingsState
}
