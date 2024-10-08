package new_structure.data.dataSource.conversation.service

import app.cash.turbine.test
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import new_structure.data.dataSource.conversation.model.ChatMessageDto
import new_structure.data.dataSource.conversation.model.OpenAiError
import new_structure.domain.util.model.Outcome
import new_structure.settings.AppSettingsFake
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val OPEN_AI_HOST = "https://api.openai.com"
private const val OPEN_AI_ENDPOINT = "v1/chat/completions"

class StreamingApiCallerImplTest {
    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, world!", response.bodyAsText())
    }

    @Test
    fun testOpenApiResponse() = testApplication {
        externalServices {
            hosts(OPEN_AI_HOST) {
                openAiSseModule()
            }
        }

        val settings = AppSettingsFake().apply { setApiKey("testKey") }
        val streamingApiCaller = StreamingApiCallerImpl(
            settings = settings,
            client = client,
        )

        streamingApiCaller.getReply(listOf(ChatMessageDto("test"))).test {
            repeat((expectedOpenAiResponse.size - 1) / 2) { index ->
                val item = awaitItem()
                assertTrue(item.isSuccess)

                val expected = expectedOpenAiResponse[index * 2]
                    .split("content\":\"")
                    .getOrNull(1)
                    ?.split("\"")
                    ?.getOrNull(0)
                    .orEmpty()
                assertEquals(item.dataOrThrow(), expected)
            }

            awaitComplete()
        }
    }

    @Test
    fun `testOpenApiResponse, no API key`() = testApplication {
        val settings = AppSettingsFake()
        val streamingApiCaller = StreamingApiCallerImpl(
            settings = settings,
            client = client,
        )

        streamingApiCaller.getReply(listOf(ChatMessageDto("test"))).test {
            val result = awaitItem()

            assertTrue(result.isFailure)
            assertEquals(result, Outcome.Failure(OpenAiError.ApiKeyError))

            awaitComplete()
        }
    }

    @Test
    fun `testOpenApiResponse, not an event stream`() = testApplication {
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

            assertTrue(result.isFailure)
            assertEquals(result, Outcome.Failure(OpenAiError.NotAnEventStreamError))

            awaitComplete()
        }
    }

    @Test
    fun `testOpenApiResponse, http error`() = testApplication {
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

            assertTrue(result.isFailure)
            assertEquals(result, Outcome.Failure(OpenAiError.HttpError(code = 403, body = "Test message.")))

            awaitComplete()
        }
    }

    private fun Application.module() {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
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
