package data.conversation.dataSource.mapper

import data.conversation.dataSource.model.ChatMessageDto
import data.conversation.dataSource.model.ChatRoleDto
import domain.conversation.data.model.MessageData
import domain.conversation.data.model.RoleData.*

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
