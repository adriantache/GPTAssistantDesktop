package new_structure.data.persona.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import new_structure.data.dataStore.DataStoreHelper
import new_structure.data.persona.mapper.toData
import new_structure.data.persona.mapper.toItem
import new_structure.data.persona.model.PersonaDataItem
import new_structure.data.util.decodeJson
import new_structure.domain.persona.data.model.PersonaData

// TODO: migrate data
private val personasKey = stringPreferencesKey("NEW_PERSONAS_KEY")

class PersonaDataSource(
    private val store: DataStore<Preferences> = DataStoreHelper.instance,
    private val json: Json = Json { encodeDefaults = true },
) {
    private suspend fun getPersonaItems(): Map<String, PersonaDataItem> {
        val preferences = store.data.firstOrNull() ?: return emptyMap()

        return preferences.decodeJson<Map<String, PersonaDataItem>>(personasKey, json).orEmpty()
    }

    suspend fun getPersonas(): Map<String, PersonaData> {
        return getPersonaItems().mapValues { it.value.toData() }
    }

    suspend fun addPersona(persona: PersonaData) {
        store.updateData {
            it.toMutablePreferences().apply {
                val personas = getPersonaItems().toMutableMap()
                personas[persona.id] = persona.toItem()

                val jsonString = json.encodeToString(personas)

                this[personasKey] = jsonString
            }
        }
    }

    suspend fun deletePersona(id: String) {
        store.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                val personas = getPersonaItems().toMutableMap()
                personas.remove(id)

                val jsonString = json.encodeToString(personas)

                this[personasKey] = jsonString
            }
        }
    }
}
