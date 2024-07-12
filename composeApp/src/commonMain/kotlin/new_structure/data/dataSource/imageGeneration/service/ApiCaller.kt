package new_structure.data.dataSource.imageGeneration.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import new_structure.data.dataSource.imageGeneration.model.OpenAiImageRequest
import new_structure.data.dataSource.imageGeneration.model.OpenAiImageResponse
import settings.AppSettings

private const val OPEN_AI_URL = "https://api.openai.com/v1/images/generations"

class ApiCaller(
    private val settings: AppSettings = AppSettings.getInstance(),
) {
    // TODO: move to DI
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    private val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 5 * 60 * 1000
            connectTimeoutMillis = 5 * 60 * 1000
            socketTimeoutMillis = 5 * 60 * 1000
        }
        install(ContentNegotiation) {
            json(json)
        }
    }

    suspend fun getImage(prompt: String): String {
        val apiKey = settings.apiKeyFlow.firstOrNull()
            ?: error("ERROR: Api key should be present!")

        val request = 0
        val requestString = json.encodeToString(request)

        val result = withContext(Dispatchers.IO) {
            client.post(OPEN_AI_URL) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $apiKey")
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody(OpenAiImageRequest(prompt))
            }
        }

        return result.body<OpenAiImageResponse>().url
    }
}
