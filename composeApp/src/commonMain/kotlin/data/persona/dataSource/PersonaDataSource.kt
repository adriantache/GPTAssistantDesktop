package data.persona.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import data.dataStore.DataStoreHelper
import data.persona.mapper.toData
import data.persona.mapper.toItem
import data.persona.model.PersonaDataItem
import data.util.decodeJson
import domain.persona.data.model.PersonaData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
