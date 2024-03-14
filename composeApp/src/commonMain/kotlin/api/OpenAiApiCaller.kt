package api

import api.model.ChatMessage
import api.model.ChatRole
import api.model.OpenAiRequest
import api.model.OpenAiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://api.openai.com"
private const val COMPLETIONS_ENDPOINT = "/v1/chat/completions"
private const val API_KEY = "***REMOVED***"

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

private class ApiConfig {
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
    }

    suspend fun getReply(conversation: List<ChatMessage>): String {
        val url = BASE_URL + COMPLETIONS_ENDPOINT

        val request = OpenAiRequest(messages = conversation)

        val response = client.post(url) {
            headers {
                append(HttpHeaders.Accept, "content/json")
                append(HttpHeaders.Authorization, "Bearer $API_KEY")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        val promptResponse: OpenAiResponse? = if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }

        return promptResponse?.choices?.firstOrNull()?.message?.content ?: response.bodyAsText()
    }
}
