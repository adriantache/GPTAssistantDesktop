package data.conversation.dataSource

import app.cash.turbine.test
import data.dataSource.conversation.service.StreamingApiCallerFake
import domain.conversation.data.model.ConversationData
import domain.conversation.data.model.MessageData
import domain.conversation.data.model.RoleData
import domain.persona.data.model.PersonaData
import domain.util.model.Outcome
import domain.util.model.TestError
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ConversationDataSourceImplTest {
    @Test
    fun `conversation with persona, api success`() = runTest {
        val conversation = ConversationData(
            id = "test",
            messages = mapOf(
                "test1" to MessageData(
                    id = "test",
                    content = "testContent",
                    role = RoleData.USER
                )
            ),
            title = "testTitle",
            persona = PersonaData(
                id = "test",
                name = "testName",
                instructions = "testInstructions",
            )
        )
        val dataSource = getDatasource(isApiSuccess = true)

        dataSource.getReplyStream(conversation).test {
            assertThat(awaitItem()).isEqualTo(Outcome.Success(data = "testInstructions"))

            awaitComplete()
        }
    }

    @Test
    fun `conversation without persona, api success`() = runTest {
        val conversation = ConversationData(
            id = "test",
            messages = mapOf(
                "test1" to MessageData(
                    id = "test",
                    content = "testContent",
                    role = RoleData.USER
                )
            ),
            title = "testTitle",
            persona = null,
        )
        val dataSource = getDatasource(isApiSuccess = true)

        dataSource.getReplyStream(conversation).test {
            assertThat(awaitItem()).isEqualTo(Outcome.Success(data = "testContent"))

            awaitComplete()
        }
    }

    @Test
    fun `conversation, api fail`() = runTest {
        val conversation = ConversationData(
            id = "test",
            messages = mapOf(
                "test1" to MessageData(
                    id = "test",
                    content = "testContent",
                    role = RoleData.USER
                )
            ),
            title = "testTitle",
            persona = PersonaData(
                id = "test",
                name = "testName",
                instructions = "testInstructions"
            )
        )
        val dataSource = getDatasource(isApiSuccess = false)

        dataSource.getReplyStream(conversation).test {
            assertThat(awaitItem()).isEqualTo(Outcome.Failure<String>(TestError()))

            awaitComplete()
        }
    }

    private fun getDatasource(isApiSuccess: Boolean): ConversationDataSource {
        return ConversationDataSourceImpl(
            apiConfig = StreamingApiCallerFake().apply {
                expectsSuccess = isApiSuccess
            }
        )
    }
}
