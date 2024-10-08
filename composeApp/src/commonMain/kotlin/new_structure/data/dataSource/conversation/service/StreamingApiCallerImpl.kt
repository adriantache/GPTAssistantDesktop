package new_structure.data.dataSource.conversation.service

import dataStore.decodeJson
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import new_structure.data.dataSource.conversation.model.ChatMessageDto
import new_structure.data.dataSource.conversation.model.OpenAiError
import new_structure.data.dataSource.conversation.model.OpenAiRequestDto
import new_structure.data.dataSource.conversation.model.OpenAiStreamingResponseDto
import new_structure.data.sse.readSse
import new_structure.domain.util.model.Outcome
import new_structure.domain.util.model.Outcome.Companion.map
import settings.AppSettings
import settings.AppSettingsImpl

private const val OPEN_AI_URL = "https://api.openai.com/v1/chat/completions"

class StreamingApiCallerImpl(
    private val settings: AppSettings = AppSettingsImpl,
    private val client: HttpClient = realHttpClient,
) : StreamingApiCaller {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    override fun getReply(conversation: List<ChatMessageDto>): Flow<Outcome<String>> {
        return flow {
            val apiKey = settings.apiKeyFlow.firstOrNull()

            if (apiKey == null) {
                emit(Outcome.Failure(OpenAiError.ApiKeyError))
                return@flow
            }

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
                .map { dataString ->
                    val messageData = dataString.decodeJson<OpenAiStreamingResponseDto>(json)

                    messageData?.choices?.firstOrNull()?.delta?.content.orEmpty()
                }

            emitAll(resultFlow)
        }
    }
}
