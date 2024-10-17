package old_code.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import new_structure.data.dataStore.DataStoreHelper
import new_structure.data.util.decodeJson
import old_code.api.model.Conversation
import kotlin.collections.set

private val conversationsKey = stringPreferencesKey("CONVERSATIONS_KEY")

class Storage private constructor(
    private val store: DataStore<Preferences> = DataStoreHelper.instance,
    private val json: Json = Json { encodeDefaults = true },
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO), // No need to link this to UI lifecycle.
) {
    val cacheFlow = store.data.map { preferences ->
        preferences.getConversationCache() ?: emptyMap()
    }

    fun updateConversation(conversation: Conversation) = scope.launch {
        store.updateData { preferences: Preferences ->
            val map = preferences.getConversationCache()
            val newMap = map?.toMutableMap() ?: mutableMapOf()

            newMap[conversation.id] = conversation

            val json = json.encodeToString(newMap)

            preferences.toMutablePreferences().apply {
                this[conversationsKey] = json
            }
        }
    }

    fun deleteConversation(id: String) = scope.launch {
        store.updateData { preferences: Preferences ->
            val map = preferences.getConversationCache()
            val newMap = map?.toMutableMap() ?: mutableMapOf()

            newMap.remove(id)

            val json = json.encodeToString(newMap)

            preferences.toMutablePreferences().apply {
                this[conversationsKey] = json
            }
        }
    }

    private fun Preferences.getConversationCache(): Map<String, Conversation>? {
        return this.decodeJson<Map<String, Conversation>>(
            key = conversationsKey,
            json = json,
        )
    }

    companion object {
        private val storage = Storage()

        fun getInstance() = storage
    }
}
