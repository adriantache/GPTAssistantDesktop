package new_structure.data.dataSource.conversation.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDto(
    val content: String,
    val role: ChatRoleDto = ChatRoleDto.user
)
