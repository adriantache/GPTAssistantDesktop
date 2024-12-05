package data.imageGeneration.service

import data.imageGeneration.model.OpenAiImageRequest
import data.imageGeneration.model.OpenAiImageResponse
import data.settings.dataSource.SettingsDataSource
import data.settings.dataSource.SettingsDataSourceImpl
import domain.imageGeneration.data.model.ImageResultData
import domain.imageGeneration.data.model.ImageResultData.ImageResultError
import domain.imageGeneration.data.model.ImageResultData.ImageResultSuccess
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private const val OPEN_AI_URL = "https://api.openai.com/v1/images/generations"

class ApiCaller(
    private val settingsDataSource: SettingsDataSource = SettingsDataSourceImpl(),
) {
    // TODO: move to DI
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

    suspend fun getImage(prompt: String): ImageResultData {
        val apiKey = settingsDataSource.getSettings().apiKey
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

        val resultBody = result.body<OpenAiImageResponse>()

        return resultBody.url?.let { ImageResultSuccess(it) }
            ?: ImageResultError(resultBody.error?.message.orEmpty())
    }
}
