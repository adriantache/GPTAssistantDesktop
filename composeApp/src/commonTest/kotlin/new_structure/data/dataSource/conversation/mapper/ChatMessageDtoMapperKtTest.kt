package new_structure.data.dataSource.conversation.mapper

import new_structure.data.conversation.dataSource.conversation.mapper.toDto
import new_structure.data.conversation.dataSource.conversation.model.ChatMessageDto
import new_structure.data.conversation.dataSource.conversation.model.ChatRoleDto
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData
import kotlin.test.Test
import kotlin.test.assertEquals

class ChatMessageDtoMapperKtTest {
    @Test
    fun `MessageData with USER role converts correctly`() {
        val messageData = MessageData(id = "test", content = "Hello, user!", role = RoleData.USER)
        val expectedDto = ChatMessageDto(content = "Hello, user!", role = ChatRoleDto.user)

        val resultDto = messageData.toDto()

        assertEquals(expectedDto.content, resultDto.content)
        assertEquals(expectedDto.role, resultDto.role)
    }

    @Test
    fun `MessageData with ASSISTANT role converts correctly`() {
        val messageData = MessageData(id = "test", content = "Hello from an assistant!", role = RoleData.ASSISTANT)
        val expectedDto = ChatMessageDto(content = "Hello from an assistant!", role = ChatRoleDto.assistant)

        val resultDto = messageData.toDto()

        assertEquals(expectedDto.content, resultDto.content)
        assertEquals(expectedDto.role, resultDto.role)
    }
}
