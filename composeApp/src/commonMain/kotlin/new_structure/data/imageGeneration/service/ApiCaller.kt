package new_structure.data.imageGeneration.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import new_structure.data.imageGeneration.model.OpenAiImageRequest
import new_structure.data.imageGeneration.model.OpenAiImageResponse
import new_structure.data.settings.dataSource.SettingsDataSource
import new_structure.data.settings.dataSource.SettingsDataSourceImpl

private const val OPEN_AI_URL = "https://api.openai.com/v1/images/generations"

class ApiCaller(
    private val settingsDataSource: SettingsDataSource = SettingsDataSourceImpl(),
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
        val apiKey = settingsDataSource.getSettings()?.apiKey
            ?: error("ERROR: Api key should be present!")

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
