package new_structure.data.conversation.dataSource.service

import io.ktor.client.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import new_structure.data.conversation.dataSource.model.ChatMessageDto
import new_structure.data.conversation.dataSource.model.OpenAiRequestDto
import new_structure.data.conversation.dataSource.model.OpenAiStreamingResponseDto
import new_structure.data.error.OpenAiError.ApiKeyError
import new_structure.data.error.OpenAiError.KtorError
import new_structure.data.httpClient.realHttpClient
import new_structure.data.settings.dataSource.SettingsDataSource
import new_structure.data.settings.dataSource.SettingsDataSourceImpl
import new_structure.data.util.decodeJson
import new_structure.domain.util.model.Outcome

private const val OPEN_AI_URL = "https://api.openai.com/v1/chat/completions"

class StreamingApiCallerImpl(
    private val settingsDataSource: SettingsDataSource = SettingsDataSourceImpl(),
    private val client: HttpClient = realHttpClient,
) : StreamingApiCaller {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    override fun getReply(conversation: List<ChatMessageDto>): Flow<Outcome<String>> {
        return flow {
            val apiKey = settingsDataSource.getSettings()?.apiKey

            if (apiKey == null) {
                emit(Outcome.Failure(ApiKeyError))
                return@flow
            }

            try {
                client.sse(
                    urlString = OPEN_AI_URL,
                    request = {
                        this.prepareRequest(
                            conversation = conversation,
                            apiKey = apiKey,
                        )
                    }
                ) {

                    incoming.collect { event ->
                        if (event.data != null && event.data != "[DONE]") {
                            val messageData = event.data.decodeJson<OpenAiStreamingResponseDto>(json)
                            val resultContent = messageData?.choices?.firstOrNull()?.delta?.content.orEmpty()

                            emit(Outcome.Success(resultContent))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(Outcome.Failure(KtorError(e)))
            }
        }
    }

    private fun HttpRequestBuilder.prepareRequest(
        conversation: List<ChatMessageDto>,
        apiKey: String,
    ) {
        val request = OpenAiRequestDto(messages = conversation, stream = true)
        val requestString = json.encodeToString(request)

        method = HttpMethod.Post

        headers {
            append(HttpHeaders.Authorization, "Bearer $apiKey")
            append(HttpHeaders.ContentType, "application/json")
        }

        setBody(requestString)
    }
}
