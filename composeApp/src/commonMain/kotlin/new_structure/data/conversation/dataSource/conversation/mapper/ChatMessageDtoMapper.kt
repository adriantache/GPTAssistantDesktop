package new_structure.data.conversation.dataSource.conversation.mapper

import new_structure.data.conversation.dataSource.conversation.model.ChatMessageDto
import new_structure.data.conversation.dataSource.conversation.model.ChatRoleDto
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData.*

fun MessageData.toDto(): ChatMessageDto {
    return ChatMessageDto(
        content = this.content,
        role = when (this.role) {
            USER -> ChatRoleDto.user
            ASSISTANT -> ChatRoleDto.assistant
            SYSTEM -> ChatRoleDto.system
        }
    )
}
