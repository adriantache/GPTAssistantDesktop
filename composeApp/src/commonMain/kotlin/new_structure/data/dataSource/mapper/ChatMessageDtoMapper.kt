package new_structure.data.dataSource.mapper

import new_structure.data.dataSource.model.ChatMessageDto
import new_structure.data.dataSource.model.ChatRoleDto
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData.ASSISTANT
import new_structure.domain.conversation.data.model.RoleData.USER

fun MessageData.toDto(): ChatMessageDto {
    return ChatMessageDto(
        content = this.content,
        role = when (this.role) {
            USER -> ChatRoleDto.user
            ASSISTANT -> ChatRoleDto.assistant
        }
    )
}
