package api

import api.model.ChatMessage
import api.model.ChatRole
import api.model.OpenAiRequest
import api.model.OpenAiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import settings.AppSettings

private const val BASE_URL = "https://api.openai.com"
private const val COMPLETIONS_ENDPOINT = "/v1/chat/completions"

// Keeping this for legacy.
class OpenAiApiCaller {
    private val config = ApiConfig()

    private val conversation = mutableListOf<ChatMessage>()

    suspend fun getReply(prompt: String): List<ChatMessage> {
        conversation += ChatMessage(prompt)

        val reply = withContext(Dispatchers.IO) {
            config.getReply(conversation)
        }

        conversation += ChatMessage(reply, ChatRole.assistant)

        return conversation.toList()
    }
}

private class ApiConfig(
    private val settings: AppSettings = AppSettings.getInstance(),
) {
    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    encodeDefaults = true
                },
                contentType = ContentType.Any
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 2 * 60 * 1000 // ChatGPT can be very slow to reply...
        }
    }

    suspend fun getReply(conversation: List<ChatMessage>): String {
        val apiKey = settings.apiKeyFlow.firstOrNull()
            ?: return "ERROR: Api key should be present!"

        val url = BASE_URL + COMPLETIONS_ENDPOINT

        val request = OpenAiRequest(messages = conversation)

        val call = kotlin.runCatching {
            client.post(url) {
                headers {
                    append(HttpHeaders.Accept, "content/json")
                    append(HttpHeaders.Authorization, "Bearer $apiKey")
                }
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }

        return call.fold(
            onSuccess = { response ->
                val promptResponse: OpenAiResponse? = if (response.status == HttpStatusCode.OK) {
                    response.body()
                } else {
                    null
                }

                promptResponse?.choices?.firstOrNull()?.message?.content ?: response.bodyAsText()
            },
            onFailure = {
                it.message.toString()
            }
        )
    }
}
