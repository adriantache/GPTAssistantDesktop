package settings

import api.model.Persona
import kotlinx.coroutines.flow.Flow

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
