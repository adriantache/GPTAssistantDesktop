package new_structure.data.conversationHistory.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData
import new_structure.domain.conversationHistory.data.model.ConversationHistoryData
import new_structure.domain.persona.data.model.PersonaData
import new_structure.domain.util.model.Outcome
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.BeforeTest
import kotlin.test.Test

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
                            title = "",
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
        persona = PersonaData(
            id = "test",
            name = "testName",
            instructions = "testInstructions"
        )
    )
}
