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

    var personas: Map<String, Persona> = emptyMap()
        get() {
            val personasString = settings.getStringOrNull(PERSONAS_KEY) ?: return emptyMap()

            // TODO: remove this legacy behaviour, or create a migration for it instead
            val legacyList = try {
                Json.decodeFromString<List<Persona>>(personasString)
            } catch (e: Exception) {
                null
            }
            if (legacyList != null) {
                val map = legacyList.associateBy { it.name }
                personas = map
                return map
            }

            return Json.decodeFromString<Map<String, Persona>>(personasString)
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
    val personasFlow: StateFlow<Map<String, Persona>> = _personasFlow

    companion object {
        private val INSTANCE = AppSettings()

        fun getInstance(): AppSettings {
            return INSTANCE
        }
    }
}
