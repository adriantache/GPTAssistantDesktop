package new_structure.data.dataSource

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import new_structure.data.dataSource.model.ChatMessageDto
import new_structure.data.dataSource.model.OpenAiRequestDto
import new_structure.data.dataSource.model.OpenAiStreamingResponseDto
import new_structure.data.sse.readSse
import settings.AppSettings

private const val OPEN_AI_URL = "https://api.openai.com/v1/chat/completions"

class StreamingApiCaller(
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

    fun getReply(conversation: List<ChatMessageDto>): Flow<String> {
        return flow {
            // TODO: better error handling
            val apiKey = settings.apiKeyFlow.firstOrNull()
                ?: error("ERROR: Api key should be present!")

            val request = OpenAiRequestDto(messages = conversation, stream = true)
            val requestString = json.encodeToString(request)

            val resultFlow = client.readSse(
                url = OPEN_AI_URL,
                requestBody = requestString,
                headers = mapOf(
                    HttpHeaders.Authorization to "Bearer $apiKey",
                    HttpHeaders.ContentType to "application/json",
                ),
            )
                .flowOn(Dispatchers.IO)
                .mapNotNull { dataString ->
                    val messageData = try {
                        json.decodeFromString<OpenAiStreamingResponseDto>(dataString)
                    } catch (e: SerializationException) {
                        null
                    }

                    messageData?.choices?.firstOrNull()?.delta?.content
                }

            emitAll(resultFlow)
        }
    }
}
