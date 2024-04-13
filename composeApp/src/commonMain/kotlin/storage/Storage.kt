package storage

import api.model.Conversation
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val CONVERSATIONS_CACHE_KEY = "CONVERSATIONS_CACHE_KEY"

class Storage(
    // TODO: use better storage, maybe files?
    private val storage: Settings = Settings(),
    private val json: Json = Json {
        encodeDefaults = true
    },
) {
    private var cache = emptyList<Conversation>()
        get() {
            val string = storage.getStringOrNull(CONVERSATIONS_CACHE_KEY) ?: return emptyList()

            return json.decodeFromString<List<Conversation>>(string)
        }
        set(value) {
            val string = json.encodeToString(value)

            storage[CONVERSATIONS_CACHE_KEY] = string
            _cacheFlow.value = value
            field = value
        }

    private val _cacheFlow = MutableStateFlow(cache)
    val cacheFlow: StateFlow<List<Conversation>> = _cacheFlow

    fun updateConversation(conversation: Conversation) = runCatching {
        if (cache.none { it.id == conversation.id }) {
            cache = cache + conversation
            return@runCatching
        }

        val newCache = cache.toMutableList()
        newCache.replaceAll { if (it.id == conversation.id) conversation else it }

        cache = newCache
    }

    fun deleteConversation(id: String) {
        val newCache = cache.toMutableList()
        newCache.removeIf { it.id == id }
        cache = newCache
    }
}
