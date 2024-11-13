package new_structure.data.dataSource.conversation

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import new_structure.data.conversation.dataSource.ConversationDataSource
import new_structure.data.conversation.dataSource.ConversationDataSourceImpl
import new_structure.data.dataSource.conversation.service.StreamingApiCallerFake
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData
import new_structure.domain.util.model.Outcome
import new_structure.domain.util.model.TestError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ConversationDestinationDataSourceTest {
    private lateinit var conversationDataSource: ConversationDataSource
    private lateinit var streamingApiCallerFake: StreamingApiCallerFake

    @BeforeTest
    fun setUp() {
        streamingApiCallerFake = StreamingApiCallerFake()
        conversationDataSource = ConversationDataSourceImpl(streamingApiCallerFake)
    }

    @Test
    fun successResponse() = runTest {
        streamingApiCallerFake.expectsSuccess = true

        val result = conversationDataSource.getReplyStream(
            ConversationData(
                id = "random",
                messages = mapOf(
                    "random" to MessageData(
                        id = "random",
                        content = "testResult",
                        role = RoleData.USER,
                    )
                ),
                title = "testTitle",
                persona = null,
            )
        )

        val expected = Outcome.Success("testResult")

        result.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun errorResponse() = runTest {
        streamingApiCallerFake.expectsSuccess = false

        val result = conversationDataSource.getReplyStream(
            ConversationData(
                id = "random",
                messages = mapOf(
                    "random" to MessageData(
                        id = "random",
                        content = "testResult",
                        role = RoleData.USER,
                    )
                ),
                title = "testTitle",
                persona = null,
            )
        )

        val expected = Outcome.Failure<String>(TestError())

        result.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
