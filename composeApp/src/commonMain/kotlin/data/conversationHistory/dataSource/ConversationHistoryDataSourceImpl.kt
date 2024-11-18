package data.conversationHistory.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import data.conversationHistory.dataSource.mapper.toData
import data.conversationHistory.dataSource.mapper.toDataEntry
import data.conversationHistory.dataSource.mapper.toHistoryData
import data.conversationHistory.dataSource.model.ConversationDataEntry
import data.dataStore.DataStoreHelper
import data.util.decodeJson
import domain.conversation.data.model.ConversationData
import domain.conversationHistory.data.model.ConversationHistoryData
import domain.util.model.Outcome
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

private val conversationsKey = stringPreferencesKey("CONVERSATIONS_KEY_NEW")

class ConversationHistoryDataSourceImpl(
    private val store: DataStore<Preferences> = DataStoreHelper.instance,
    private val json: Json = Json { encodeDefaults = true },
) : ConversationHistoryDataSource {
    private val cacheFlow = store.data.map { preferences ->
        preferences.getConversationCache()
    }

    override suspend fun saveConversation(conversationData: ConversationData) {
        store.updateData { preferences: Preferences ->
            val map = preferences.getConversationCache()
            val newMap = map?.toMutableMap() ?: mutableMapOf()

            newMap[conversationData.id] = conversationData.toDataEntry()

            val json = json.encodeToString(newMap)

            preferences.toMutablePreferences().apply {
                this[conversationsKey] = json
            }
        }
    }

    override suspend fun getConversations(
        searchText: String?,
        startDateTime: LocalDateTime?,
        endDateTime: LocalDateTime?
    ): Outcome.Success<List<ConversationHistoryData>?> {
        val conversations = cacheFlow.first()

        return Outcome.Success(conversations?.values?.map { it.toHistoryData() }?.reversed())
    }

    override suspend fun deleteConversation(id: String) {
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

    override suspend fun getConversation(id: String): ConversationData {
        val conversations = cacheFlow.firstOrNull()

        return conversations?.get(id)?.toData() ?: error("Could not retrieve conversation!")
    }

    // Not using Outcome here because we do want a crash if something is wrong with the data.
    private fun Preferences.getConversationCache(): Map<String, ConversationDataEntry>? {
        return this.decodeJson<Map<String, ConversationDataEntry>>(
            key = conversationsKey,
            json = json,
        )
    }
}
