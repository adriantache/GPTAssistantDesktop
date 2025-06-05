package data.conversationHistory.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import domain.conversation.data.model.ConversationData
import domain.conversation.data.model.MessageData
import domain.conversation.data.model.RoleData
import domain.conversationHistory.data.model.ConversationHistoryData
import domain.persona.data.model.PersonaData
import domain.util.model.Outcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("Disabled due to Android test failure.")
class ConversationHistoryDataSourceImplTest {
    private lateinit var dataSource: ConversationHistoryDataSource
    private lateinit var store: DataStore<Preferences>

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = CoroutineScope(UnconfinedTestDispatcher())

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @BeforeTest
    fun setUp() = runTest {
        store = PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = { tmpFolder.newFile("user.preferences_pb") }
        )

        dataSource = ConversationHistoryDataSourceImpl(
            store = store,
        )
    }

    @Test
    fun saveConversation() = runTest {
        dataSource.saveConversation(conversation)

        val result = dataSource.getConversations()
        assertThat(result)
            .isEqualTo(
                Outcome.Success(
                    listOf(
                        ConversationHistoryData(
                            id = "test",
                            title = "testTitle",
                            date = result.dataOrThrow()[0].date
                        )
                    )
                )
            )
    }

    @Test
    fun deleteConversation() = runTest {
        dataSource.saveConversation(conversation)
        dataSource.deleteConversation(conversation.id)

        val result: Outcome<List<ConversationHistoryData>?> = dataSource.getConversations()
        assertThat(result)
            .isEqualTo(Outcome.Success<List<ConversationHistoryData>?>(emptyList()))
    }

    @Test
    fun getConversation() = runTest {
        dataSource.saveConversation(conversation)

        assertThat(dataSource.getConversation(conversation.id))
            .isEqualTo(conversation)
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
