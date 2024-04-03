package api

import api.model.ChatMessage
import api.model.ChatRole
import api.model.OpenAiRequest
import api.model.OpenAiStreamingResponse
import api.sse.readSse
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import settings.AppSettings

private const val BASE_URL = "https://api.openai.com"
private const val COMPLETIONS_ENDPOINT = "/v1/chat/completions"
private const val LOADING_MESSAGE = "..."

class OpenAiStreamingApiCaller {
    private val config = StreamingApiConfig()

    private val conversation = mutableListOf<ChatMessage>()

    suspend fun getReply(prompt: String): Flow<List<ChatMessage>> {
        return flow {
            conversation += ChatMessage(prompt)

            var chatMessage = ChatMessage(LOADING_MESSAGE, ChatRole.assistant)

            conversation += chatMessage
            emit(conversation.toList())

            config.getReply(conversation)
                .flowOn(Dispatchers.IO)
                .collect {
                    val newContent = if (chatMessage.content == LOADING_MESSAGE) it else chatMessage.content + it

                    chatMessage = chatMessage.copy(content = newContent)

                    conversation.replaceAll { message ->
                        if (message.id == chatMessage.id) {
                            chatMessage
                        } else {
                            message
                        }
                    }

                    emit(conversation.toList())
                }
        }
    }

    private class StreamingApiConfig(
        private val settings: AppSettings = AppSettings.getInstance(),
    ) {
        private val client = HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 5 * 60 * 1000 // ChatGPT can be very slow to reply, even when streaming...
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        private val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = true
        }

        fun getReply(conversation: List<ChatMessage>): Flow<String> {
            val apiKey = settings.apiKey ?: return flowOf("ERROR: Api key should be present!")

            val url = BASE_URL + COMPLETIONS_ENDPOINT

            val request = OpenAiRequest(messages = conversation, stream = true)

            val requestString = json.encodeToString(request)

            return client.readSse(
                url = url,
                headersProvider = {
                    if (it == null) {
                        mapOf(
                            HttpHeaders.Authorization to "Bearer $apiKey",
                            HttpHeaders.ContentType to "application/json",
                        )
                    } else emptyMap()
                },
                requestBody = requestString,
            ).mapNotNull { sseEvent ->
                val messageData = try {
                    json.decodeFromString<OpenAiStreamingResponse>(sseEvent.data)
                } catch (e: SerializationException) {
                    null
                }

                messageData?.choices?.firstOrNull()?.delta?.content
            }
        }
    }
}
