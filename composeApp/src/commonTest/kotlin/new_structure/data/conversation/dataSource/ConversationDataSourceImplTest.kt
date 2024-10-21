package new_structure.data.conversation.dataSource

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import new_structure.data.dataSource.conversation.service.StreamingApiCallerFake
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData
import new_structure.domain.persona.data.model.PersonaData
import new_structure.domain.util.model.Outcome
import new_structure.domain.util.model.TestError
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
            persona = PersonaData(
                id = "test",
                name = "testName",
                instructions = "testInstructions"
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
