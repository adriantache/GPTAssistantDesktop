package old_code.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import new_structure.domain.util.model.Outcome
import old_code.api.model.*
import old_code.api.sse.readSse
import old_code.dataStore.decodeJson
import old_code.settings.AppSettings
import old_code.settings.AppSettingsImpl

private const val BASE_URL = "https://api.openai.com"
private const val COMPLETIONS_ENDPOINT = "/v1/chat/completions"
private const val LOADING_MESSAGE = "..."

class OpenAiStreamingApiCaller(
    private val settings: AppSettings = AppSettingsImpl,
) {
    private val config = StreamingApiConfig()

    private var conversation = Conversation()

    fun reset(): Conversation {
        conversation = Conversation()
        addPersona()
        return conversation
    }

    private fun addPersona() {
        val persona = settings.selectedPersona ?: return

        conversation = conversation.add(ChatMessage(persona.instructions))
    }

    fun setConversation(conversation: Conversation) {
        this.conversation = conversation
    }

    fun getReply(prompt: String): Flow<Conversation> {
        return flow {
            conversation = conversation.add(ChatMessage(prompt))

            var chatMessage = ChatMessage(LOADING_MESSAGE, ChatRole.assistant)

            conversation = conversation.add(chatMessage)
            emit(conversation)

            config.getReply(conversation.contents)
                .flowOn(Dispatchers.IO)
                .collect {
                    val newContent = if (chatMessage.content == LOADING_MESSAGE) it else chatMessage.content + it

                    chatMessage = chatMessage.copy(content = newContent)

                    conversation = conversation.update(chatMessage)

                    emit(conversation)
                }
        }
    }

    private class StreamingApiConfig(
        private val settings: AppSettings = AppSettingsImpl,
    ) {
        private val client = HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 5 * 60 * 1000 // ChatGPT can be very slow to reply, even when streaming...
            }
        }

        private val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = true
        }

        suspend fun getReply(conversation: List<ChatMessage>): Flow<String> {
            val apiKey = settings.apiKeyFlow.firstOrNull()
                ?: return flowOf("ERROR: Api key should be present!")

            val url = BASE_URL + COMPLETIONS_ENDPOINT

            val request = OpenAiRequest(messages = conversation, stream = true)
            val requestString = json.encodeToString(request)

            return client.readSse(
                url = url,
                requestBody = requestString,
                headers = mapOf(
                    HttpHeaders.Authorization to "Bearer $apiKey",
                    HttpHeaders.ContentType to "application/json",
                ),
            )
                .flowOn(Dispatchers.IO)
                .mapNotNull { outcome ->
                    val dataString = (outcome as Outcome.Success).data
                    val messageData = dataString.decodeJson<OpenAiStreamingResponse>(json)

                    messageData?.choices?.firstOrNull()?.delta?.content
                }
        }
    }
}
