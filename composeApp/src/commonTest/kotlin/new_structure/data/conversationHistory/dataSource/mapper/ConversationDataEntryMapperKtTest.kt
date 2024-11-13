package new_structure.data.conversationHistory.dataSource.mapper

import new_structure.data.conversationHistory.dataSource.model.ConversationDataEntry
import new_structure.data.conversationHistory.dataSource.model.MessageDataEntry
import new_structure.data.conversationHistory.dataSource.model.PersonaDataEntry
import new_structure.data.conversationHistory.dataSource.model.RoleDataEntry
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData
import new_structure.domain.conversationHistory.data.model.ConversationHistoryData
import new_structure.domain.persona.data.model.PersonaData
import org.assertj.core.api.Assertions.assertThat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test

class ConversationDataEntryMapperKtTest {
    @Test
    fun toDataEntry() {
        val input = conversation
        val expected = conversationDataEntry

        assertThat(input.toDataEntry(createdAt = expected.createdAt)).isEqualTo(expected)
    }

    @Test
    fun roleData() {
        RoleData.entries.forEachIndexed { i, role ->
            assertThat(role.toDataEntry()).isEqualTo(RoleDataEntry.entries[i])
        }
    }

    @Test
    fun toData() {
        val input = conversationDataEntry
        val expected = conversation

        assertThat(input.toData()).isEqualTo(expected)
    }

    @Test
    fun roleDataEntry() {
        RoleDataEntry.entries.forEachIndexed { i, role ->
            assertThat(role.toData()).isEqualTo(RoleData.entries[i])
        }
    }

    @Test
    fun toHistoryData() {
        val input = conversationDataEntry
        val expected = ConversationHistoryData(
            id = "test",
            title = "testTitle",
            date = LocalDateTime.ofInstant(Instant.ofEpochMilli(input.createdAt), ZoneId.systemDefault()),
        )

        assertThat(input.toHistoryData()).isEqualTo(expected)
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

    private val conversationDataEntry = ConversationDataEntry(
        id = "test",
        title = "testTitle",
        createdAt = Instant.now().toEpochMilli(),
        messages = mapOf(
            "test1" to MessageDataEntry(
                id = "test",
                content = "testContent",
                role = RoleDataEntry.USER
            )
        ),
        persona = PersonaDataEntry(
            id = "test",
            name = "testName",
            instructions = "testInstructions"
        )
    )
}
