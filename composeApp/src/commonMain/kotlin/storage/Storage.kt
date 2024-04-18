package storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import api.model.Conversation
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platformSpecific.dataStorePreferences
import kotlin.collections.set

private val conversationsKey = stringPreferencesKey("CONVERSATIONS_KEY")

class Storage private constructor(
    private val store: DataStore<Preferences> = dataStorePreferences(),
    private val json: Json = Json { encodeDefaults = true },
) {
    val cacheFlow by lazy {
        store.data.map {
            it.decodeConversationJson() ?: emptyMap()
        }
    }

    suspend fun updateConversation(conversation: Conversation) {
        store.updateData { preferences: Preferences ->
            val map = preferences.decodeConversationJson()
            val newMap = map?.toMutableMap() ?: mutableMapOf()

            newMap[conversation.id] = conversation

            val json = json.encodeToString(newMap)

            preferences.toMutablePreferences().apply {
                this[conversationsKey] = json
            }
        }
    }

    suspend fun deleteConversation(id: String) {
        store.updateData { preferences: Preferences ->
            val map = preferences.decodeConversationJson()
            val newMap = map?.toMutableMap() ?: mutableMapOf()

            newMap.remove(id)

            val json = json.encodeToString(newMap)

            preferences.toMutablePreferences().apply {
                this[conversationsKey] = json
            }
        }
    }

    private fun Preferences.decodeConversationJson(): Map<String, Conversation>? {
        val string = this[conversationsKey] ?: return null

        return json.decodeFromString<Map<String, Conversation>>(string)
    }

    companion object {
        private val storage = Storage()

        fun getInstance() = storage
    }
}
