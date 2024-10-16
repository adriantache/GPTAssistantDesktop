package new_structure.data.dataSource.conversation.service

import app.cash.turbine.test
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import new_structure.data.conversation.dataSource.conversation.model.ChatMessageDto
import new_structure.data.conversation.dataSource.conversation.service.StreamingApiCallerImpl
import new_structure.data.error.OpenAiError.ApiKeyError
import new_structure.data.error.OpenAiError.KtorError
import new_structure.settings.AppSettingsFake
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val OPEN_AI_HOST = "https://api.openai.com"
private const val OPEN_AI_ENDPOINT = "v1/chat/completions"
private const val OPEN_AI_URL = "https://api.openai.com/v1/chat/completions"

class StreamingApiCallerImplTest {
    @Test
    fun testRoot() = runTest {
        val expected = "Hello, world!"
        val engine = MockEngine { respondOk(expected) }
        val client = HttpClient(engine)

        val response = client.get("/")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo(expected)
    }

    @Test
    fun testOpenApiResponse() = testApplication {
        externalServices {
            hosts(OPEN_AI_HOST) {
                openAiSseModule()
            }
        }

        val client = createClient {
            install(SSE)
        }

        val settings = AppSettingsFake().apply { setApiKey("testKey") }
        val streamingApiCaller = StreamingApiCallerImpl(
            settings = settings,
            client = client,
        )

        streamingApiCaller.getReply(listOf(ChatMessageDto("test"))).test {
            repeat((expectedOpenAiResponse.size - 1) / 2) { index ->
                val item = awaitItem()
                assertThat(item.isSuccess).isTrue()

                val expected = expectedOpenAiResponse[index * 2]
                    .split("content\":\"")
                    .getOrNull(1)
                    ?.split("\"")
                    ?.getOrNull(0)
                    .orEmpty()
                assertThat(item.dataOrThrow()).isEqualTo(expected)
            }

            awaitComplete()
        }
    }

    @Test
    fun `testOpenApiResponse, no API key`() = testApplication {
        val client = createClient {
            install(SSE)
        }

        val settings = AppSettingsFake()
        val streamingApiCaller = StreamingApiCallerImpl(
            settings = settings,
            client = client,
        )

        streamingApiCaller.getReply(listOf(ChatMessageDto("test"))).test {
            val result = awaitItem()

            assertThat(result.isFailure).isTrue()
            assertThat(result.errorOrThrow()).isEqualTo(ApiKeyError)

            awaitComplete()
        }
    }

    @Test
    fun `testOpenApiResponse, not an event stream`() = testApplication {
        val client = createClient {
            install(SSE)
        }

        externalServices {
            hosts(OPEN_AI_HOST) {
                openAiSseModule(simulateEventStreamError = true)
            }
        }

        val settings = AppSettingsFake().apply { setApiKey("testKey") }
        val streamingApiCaller = StreamingApiCallerImpl(
            settings = settings,
            client = client,
        )

        streamingApiCaller.getReply(listOf(ChatMessageDto("test"))).test {
            val result = awaitItem()

            assertThat(result.isFailure).isTrue()
            assertThat(result.errorOrThrow()).isInstanceOf(KtorError::class.java)
            assertThat((result.errorOrThrow() as KtorError).error).isInstanceOf(SSEClientException::class.java)
            assertThat(result.errorOrThrow().message)
                .isEqualTo("Expected Content-Type text/event-stream but was application/octet-stream")

            awaitComplete()
        }
    }

    @Test
    fun `testOpenApiResponse, http error`() = testApplication {
        val client = createClient {
            install(SSE)
        }

        externalServices {
            hosts(OPEN_AI_HOST) {
                openAiSseModule(simulateHttpError = true)
            }
        }

        val settings = AppSettingsFake().apply { setApiKey("testKey") }
        val streamingApiCaller = StreamingApiCallerImpl(
            settings = settings,
            client = client,
        )

        streamingApiCaller.getReply(listOf(ChatMessageDto("test"))).test {
            val result = awaitItem()

            assertThat(result.isFailure).isTrue()
            assertThat(result.errorOrThrow()).isInstanceOf(KtorError::class.java)
            assertThat((result.errorOrThrow() as KtorError).error).isInstanceOf(SSEClientException::class.java)
            assertThat(result.errorOrThrow().message).isEqualTo("Expected status code 200 but was 403")

            awaitComplete()
        }
    }

    // TODO: when MockEngine supports SSE, use this solution instead and remove server test dependency
    @Suppress("unused")
    private fun getOpenAiSseFakeClient(): HttpClient {
        val engine = MockEngine { request ->
            assertEquals(HttpMethod.Post, request.method)
            assertEquals(OPEN_AI_URL, request.url.toString())
            assertTrue(request.headers.contains(HttpHeaders.Authorization))
            assertTrue(request.headers[HttpHeaders.Authorization]!!.length > 7)
            assertTrue(request.headers.contains(HttpHeaders.ContentType))
            assertEquals("application/json", request.headers[HttpHeaders.ContentType])

            val channel = ByteChannel(autoFlush = true)

            runBlocking {
                expectedOpenAiResponse.forEach {
                    val payload = if (it.isEmpty()) "\n" else "data: $it\n"

                    channel.writeStringUtf8(payload)
                }

                channel.close()
            }

            respond(channel)
        }

        return HttpClient(engine) {
            install(SSE)
        }
    }

    private fun Application.openAiSseModule(
        simulateEventStreamError: Boolean = false,
        simulateHttpError: Boolean = false,
    ) {
        val contentType = if (simulateEventStreamError) {
            null
        } else {
            ContentType(
                contentType = "text",
                contentSubtype = "event-stream",
            )
        }

        routing {
            post(OPEN_AI_ENDPOINT) {
                if (simulateHttpError) {
                    call.respond(HttpStatusCode.Forbidden, "Test message.")
                } else {
                    call.respondOutputStream(
                        contentType = contentType
                    ) {
                        expectedOpenAiResponse.forEach {
                            val payload = if (it.isEmpty()) "\n" else "data: $it\n"

                            write(payload.toByteArray())
                            flush()
                        }
                    }
                }
            }
        }
    }

    private val expectedOpenAiResponse = listOf(
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"role\":\"assistant\",\"content\":\"\",\"refusal\":\"\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"Sure\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\",\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" I\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" am\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" here\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" to\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" help\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"!\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" If\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" you\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" have\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" any\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" questions\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" or\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" need\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" assistance\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\",\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" feel\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" free\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" to\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" ask\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\".\"},\"logprobs\":\"\",\"finish_reason\":\"\"}]}",
        "",
        "{\"id\":\"chatcmpl-AEdm9ovhO3SpdEF1oJIi9DCcUdsIz\",\"object\":\"chat.completion.chunk\",\"created\":1728052853,\"model\":\"gpt-4o-2024-08-06\",\"system_fingerprint\":\"fp_e5e4913e83\",\"choices\":[{\"index\":0,\"delta\":{},\"logprobs\":\"\",\"finish_reason\":\"stop\"}]}",
        "",
        "[DONE]",
    )
}
