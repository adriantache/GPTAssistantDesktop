package new_structure.data.conversation.dataSource.conversation.service

import kotlinx.coroutines.flow.Flow
import new_structure.data.conversation.dataSource.conversation.model.ChatMessageDto
import new_structure.domain.util.model.Outcome

interface StreamingApiCaller {
    fun getReply(conversation: List<ChatMessageDto>): Flow<Outcome<String>>
}
