package storage

import api.model.Conversation
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import kotlin.collections.set

class Storage private constructor(
    // TODO: use better storage, maybe files?
) {
    private val store: KStore<Map<String, Conversation>> by lazy { initStore() }

    val cacheFlow by lazy { store.updates.filterNotNull() }

    suspend fun updateConversation(conversation: Conversation) {
        store.update {
            val newMap = it?.toMutableMap() ?: mutableMapOf()

            newMap.apply {
                this[conversation.id] = conversation
            }
        }
    }

    suspend fun deleteConversation(id: String) {
        store.update {
            it?.toMutableMap()?.apply {
                this.remove(id)
            }
        }
    }

    private fun initStore(): KStore<Map<String, Conversation>> {
        return storeOf(
            file = "conversations.json".toPath(),
            //        file = "${providePath()}/conversations.json".toPath(),
            json = Json { encodeDefaults = true },
        )
    }

    companion object {
        private val storage = Storage()

        fun getInstance() = storage
    }
}
