package data.migration.legacy

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import data.dataStore.DataStoreHelper
import data.util.decodeJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private val API_KEY_KEY = stringPreferencesKey("API_KEY_KEY")
private val PERSONAS_KEY = stringPreferencesKey("PERSONAS_KEY")


// TODO: move to DI and revert constructor
object AppSettingsImpl {
    private val store: DataStore<Preferences> = DataStoreHelper.instance
    private val json: Json = Json { encodeDefaults = true }
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO) // No need to link this to UI lifecycle.

    val apiKeyFlow = store.data.map { preferences ->
        preferences[API_KEY_KEY]
    }

    val personasFlow = store.data.map { preferences ->
        preferences.decodeJson<Map<String, Persona>>(key = PERSONAS_KEY, json = json) ?: emptyMap()
    }

    fun clearApiKey() {
        scope.launch {
            store.edit {
                it.remove(API_KEY_KEY)
            }
        }
    }
}
