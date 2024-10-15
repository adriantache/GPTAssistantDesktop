package new_structure.settings

import kotlinx.coroutines.flow.MutableStateFlow
import old_code.api.model.Persona
import old_code.settings.AppSettings

class AppSettingsFake : AppSettings {
    override var selectedPersona: Persona? = null

    override val apiKeyFlow: MutableStateFlow<String?> = MutableStateFlow(null)

    override val forceDarkModeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val personasFlow: MutableStateFlow<Map<String, Persona>> = MutableStateFlow(emptyMap())

    override fun setApiKey(apiKey: String) {
        apiKeyFlow.value = apiKey
    }

    override fun clearApiKey() {
        apiKeyFlow.value = null
    }

    override fun setForceDarkMode(forceDarkMode: Boolean) {
        forceDarkModeFlow.value = forceDarkMode
    }

    override fun setPersonas(personas: Map<String, Persona>) {
        personasFlow.value = personas
    }
}
