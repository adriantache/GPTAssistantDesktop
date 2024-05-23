package new_structure.data.dataSource.persona

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dataStore.DataStoreHelper
import dataStore.decodeJson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import new_structure.domain.conversation.data.model.PersonaData

// TODO: migrate data
private val personasKey = stringPreferencesKey("NEW_PERSONAS_KEY")

class PersonaDataSource(
    private val store: DataStore<Preferences> = DataStoreHelper.getInstance(),
    private val json: Json = Json { encodeDefaults = true },
) {
    suspend fun getPersonas(): List<PersonaData> {
        val preferences = store.data.firstOrNull() ?: return emptyList()

        return preferences.decodeJson<List<PersonaData>>(personasKey, json).orEmpty()
    }

    suspend fun addPersona(persona: PersonaData) {
        store.updateData {
            it.toMutablePreferences().apply {
                val personas = this.decodeJson<List<PersonaData>>(personasKey, json).orEmpty()
                val newList = personas + persona

                val jsonString = json.encodeToString(newList)

                this[personasKey] = jsonString
            }
        }
    }

    suspend fun deletePersona(id: String) {
        store.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                val personas = this.decodeJson<List<PersonaData>>(personasKey, json).orEmpty()
                val newList = personas.toMutableList().removeIf { it.id == id }

                val jsonString = json.encodeToString(newList)

                this[personasKey] = jsonString
            }
        }
    }
}
