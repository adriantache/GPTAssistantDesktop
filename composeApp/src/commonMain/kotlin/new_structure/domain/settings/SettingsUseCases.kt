package new_structure.domain.settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import new_structure.data.settings.SettingsRepositoryImpl
import new_structure.domain.settings.data.SettingsRepository
import new_structure.domain.settings.entity.ApiKeyInput
import new_structure.domain.settings.state.SettingsState
import new_structure.domain.settings.state.SettingsState.*

object SettingsUseCases {
    private val repository: SettingsRepository = SettingsRepositoryImpl()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private var apiKeyInput = ApiKeyInput()

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(Init(::onInit))
    val state: StateFlow<SettingsState> = _state

    private fun onInit() {
        checkApiKey()
    }

    private fun checkApiKey() {
        scope.launch {
            val settings = repository.getSettings()

            if (settings.apiKey == null) {
                showMissingApiKey()
                return@launch
            }

            setIdle()
        }
    }

    private fun showMissingApiKey() {
        _state.value = MissingApiKey(
            apiKey = apiKeyInput.input,
            onInput = {
                apiKeyInput = apiKeyInput.onInput(it)
                showMissingApiKey()
            },
            canSubmit = apiKeyInput.isValid,
            onSubmit = {
                scope.launch {
                    repository.setApiKey(apiKeyInput.input)

                    checkApiKey()
                }
            },
        )
    }

    private fun onDisplaySettings() {
        _state.value = SettingsVisible(
            onClearApiKey = ::clearApiKey,
            onDismiss = ::setIdle,
        )
    }

    private fun setIdle() {
        _state.value = Idle(
            onDisplaySettings = ::onDisplaySettings,
        )
    }

    private fun clearApiKey() {
        scope.launch {
            repository.setApiKey(null)
        }
    }
}
