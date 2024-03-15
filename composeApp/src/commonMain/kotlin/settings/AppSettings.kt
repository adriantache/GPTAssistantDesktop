package settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val API_KEY_KEY = "API_KEY_KEY"

class AppSettings private constructor() {
    private val settings: Settings = Settings()

    var apiKey: String? = null
        get() = settings.getStringOrNull(API_KEY_KEY)
        set(value) {
            settings[API_KEY_KEY] = value
            _apiKeyFlow.value = value
            field = value
        }

    private val _apiKeyFlow = MutableStateFlow(apiKey)
    val apiKeyFlow: StateFlow<String?> = _apiKeyFlow

    companion object {
        private val INSTANCE = AppSettings()

        fun getInstance(): AppSettings {
            return INSTANCE
        }
    }
}