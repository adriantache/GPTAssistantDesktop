package settings

import api.model.Persona
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val API_KEY_KEY = "API_KEY_KEY"
private const val FORCE_DARK_MODE_KEY = "FORCE_DARK_MODE_KEY"
private const val PERSONAS_KEY = "PERSONAS_KEY"

class AppSettings private constructor() {
    private val settings: Settings = Settings()

    var apiKey: String? = null
        get() = settings.getStringOrNull(API_KEY_KEY)
        set(value) {
            settings[API_KEY_KEY] = value
            _apiKeyFlow.value = value
            field = value
        }

    var forceDarkMode: Boolean = false
        get() = settings.getBoolean(FORCE_DARK_MODE_KEY, false)
        set(value) {
            settings[FORCE_DARK_MODE_KEY] = value
            _forceDarkModeFlow.value = value
            field = value
        }

    var selectedPersona: Persona? = null

    var personas: List<Persona> = emptyList()
        get() {
            val personasString = settings.getStringOrNull(PERSONAS_KEY) ?: return emptyList()
            return Json.decodeFromString<List<Persona>>(personasString)
        }
        set(value) {
            val json = Json.encodeToString(value)
            settings[PERSONAS_KEY] = json
            _personasFlow.value = value
            field = value
        }

    private val _apiKeyFlow = MutableStateFlow(apiKey)
    val apiKeyFlow: StateFlow<String?> = _apiKeyFlow

    private val _forceDarkModeFlow = MutableStateFlow(forceDarkMode)
    val forceDarkModeFlow: StateFlow<Boolean> = _forceDarkModeFlow

    private val _personasFlow = MutableStateFlow(personas)
    val personasFlow: StateFlow<List<Persona>> = _personasFlow

    companion object {
        private val INSTANCE = AppSettings()

        fun getInstance(): AppSettings {
            return INSTANCE
        }
    }
}
