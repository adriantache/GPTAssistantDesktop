package settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import api.model.Persona
import dataStore.DataStoreHelper
import dataStore.decodeJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val API_KEY_KEY = stringPreferencesKey("API_KEY_KEY")
private val FORCE_DARK_MODE_KEY = booleanPreferencesKey("FORCE_DARK_MODE_KEY")
private val PERSONAS_KEY = stringPreferencesKey("PERSONAS_KEY")

class AppSettings private constructor(
    private val store: DataStore<Preferences> = DataStoreHelper.getInstance(),
    private val json: Json = Json { encodeDefaults = true },
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO), // No need to link this to UI lifecycle.
) {
    var selectedPersona: Persona? = null

    val apiKeyFlow = store.data.map { preferences ->
        preferences[API_KEY_KEY]
    }

    val forceDarkModeFlow = store.data.map { preferences ->
        preferences[FORCE_DARK_MODE_KEY] ?: false
    }

    val personasFlow = store.data.map { preferences ->
        preferences.decodeJson<Map<String, Persona>>(key = PERSONAS_KEY, json = json) ?: emptyMap()
    }

    fun setApiKey(apiKey: String) = scope.launch {
        store.edit {
            it[API_KEY_KEY] = apiKey
        }
    }

    fun clearApiKey() = scope.launch {
        store.edit {
            it.remove(API_KEY_KEY)
        }
    }

    fun setForceDarkMode(forceDarkMode: Boolean) = scope.launch {
        store.edit {
            it[FORCE_DARK_MODE_KEY] = forceDarkMode
        }
    }

    fun setPersonas(personas: Map<String, Persona>) = scope.launch {
        val personasJson = json.encodeToString(personas)

        store.edit {
            it[PERSONAS_KEY] = personasJson
        }
    }

    companion object {
        private val INSTANCE = AppSettings()

        fun getInstance(): AppSettings {
            return INSTANCE
        }
    }
}
