package data.conversationHistory

import data.conversationHistory.dataSource.ConversationHistoryDataSourceFake
import domain.conversation.data.model.ConversationData
import domain.conversation.data.model.MessageData
import domain.conversation.data.model.RoleData
import domain.conversationHistory.data.model.ConversationHistoryData
import domain.persona.data.model.PersonaData
import domain.util.model.Outcome
import domain.util.model.TestError
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime
import kotlin.test.Test

class ConversationHistoryRepositoryImplTest {
    @Test
    fun getConversations() = runTest {
        val date = LocalDateTime.now()
        val date2 = LocalDateTime.now().plusDays(1)

        val dataSourceFake = getDatasource(Outcome.Success(emptyList()))
        val repository = ConversationHistoryRepositoryImpl(dataSourceFake)

        assertThat(
            repository.getConversations(
                searchText = "search",
                startDateTime = date,
                endDateTime = date2,
            )
        ).isEqualTo(Outcome.Success<List<ConversationHistoryData>?>(emptyList()))
        assertThat(dataSourceFake.searchText).isEqualTo("search")
        assertThat(dataSourceFake.startDateTime).isEqualTo(date)
        assertThat(dataSourceFake.endDateTime).isEqualTo(date2)
    }

    @Test
    fun `getConversations, error`() = runTest {
        val date = LocalDateTime.now()

        val dataSourceFake = getDatasource(Outcome.Failure(TestError()))
        val repository = ConversationHistoryRepositoryImpl(dataSourceFake)

        assertThat(
            repository.getConversations(
                searchText = "search",
                startDateTime = date,
                endDateTime = date,
            )
        ).isEqualTo(Outcome.Failure<List<ConversationHistoryData>?>(TestError()))
    }

    @Test
    fun deleteConversation() = runTest {
        val dataSourceFake = getDatasource()
        val repository = ConversationHistoryRepositoryImpl(dataSourceFake)

        repository.deleteConversation("test")

        assertThat(dataSourceFake.deletedId).isEqualTo("test")
    }

    @Test
    fun saveConversation() = runTest {
        val dataSourceFake = getDatasource()
        val repository = ConversationHistoryRepositoryImpl(dataSourceFake)

        repository.saveConversation(conversation)

        assertThat(dataSourceFake.savedConversation).isEqualTo(conversation)
    }

    @Test
    fun getConversation() = runTest {
        val dataSourceFake = getDatasource()
        val repository = ConversationHistoryRepositoryImpl(dataSourceFake)
        repository.saveConversation(conversation)

        assertThat(repository.getConversation("test")).isEqualTo(conversation)

        assertThat(dataSourceFake.conversationId).isEqualTo("test")
    }

    private fun getDatasource(
        conversationsOutput: Outcome<List<ConversationHistoryData>?> = Outcome.Success(emptyList()),
    ): ConversationHistoryDataSourceFake {
        return ConversationHistoryDataSourceFake(
            conversationsOutput = conversationsOutput
        )
    }

    private val conversation = ConversationData(
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
}
