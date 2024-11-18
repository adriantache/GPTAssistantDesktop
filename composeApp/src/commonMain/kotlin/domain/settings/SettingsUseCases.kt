package domain.settings

import data.settings.SettingsRepositoryImpl
import domain.settings.data.SettingsRepository
import domain.settings.entity.ApiKeyInput
import domain.settings.state.SettingsState
import domain.settings.state.SettingsState.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                    apiKeyInput = ApiKeyInput() // Clear this input so we don't leak the API key.

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

            checkApiKey()
        }
    }
}
