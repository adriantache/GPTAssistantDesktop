package old_code.settings

import kotlinx.coroutines.flow.Flow
import old_code.api.model.Persona

interface AppSettings {
    var selectedPersona: Persona?
    val apiKeyFlow: Flow<String?>
    val forceDarkModeFlow: Flow<Boolean>
    val personasFlow: Flow<Map<String, Persona>>

    fun setApiKey(apiKey: String)
    fun clearApiKey()
    fun setForceDarkMode(forceDarkMode: Boolean)
    fun setPersonas(personas: Map<String, Persona>)
}
