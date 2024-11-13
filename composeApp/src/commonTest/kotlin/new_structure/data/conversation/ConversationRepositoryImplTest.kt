package new_structure.data.conversation

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import new_structure.data.conversation.dataSource.ConversationDataSourceFake
import new_structure.domain.conversation.data.ConversationRepository
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.util.model.Outcome
import new_structure.domain.util.model.TestError
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ConversationRepositoryImplTest {
    @Test
    fun getReplyStream() = runTest {
        val input = conversationDummy
        val expected = Outcome.Success("success")

        val repository = getRepository(expected)

        repository.getReplyStream(input).test {
            assertThat(awaitItem()).isEqualTo(expected)

            awaitComplete()
        }
    }

    @Test
    fun `getReplyStream, error`() = runTest {
        val input = conversationDummy
        val expected = Outcome.Failure<String>(TestError())

        val repository = getRepository(expected)

        repository.getReplyStream(input).test {
            assertThat(awaitItem()).isEqualTo(expected)

            awaitComplete()
        }
    }

    private fun getRepository(result: Outcome<String>): ConversationRepository {
        val dataSource = ConversationDataSourceFake(flowOf(result))

        return ConversationRepositoryImpl(conversationDataSource = dataSource)
    }

    private val conversationDummy = ConversationData(
        id = "test",
        messages = emptyMap(),
        persona = null,
        title = "testTitle",
    )
}
