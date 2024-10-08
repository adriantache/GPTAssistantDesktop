package new_structure.data.dataSource.conversation

import kotlinx.coroutines.flow.Flow
import new_structure.data.dataSource.conversation.mapper.toDto
import new_structure.data.dataSource.conversation.service.StreamingApiCaller
import new_structure.data.dataSource.conversation.service.StreamingApiCallerImpl
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.util.model.Outcome

class ConversationDataSource(
    private val apiConfig: StreamingApiCaller = StreamingApiCallerImpl(),
) {
    fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>> {
        return apiConfig.getReply(conversation.messages.map { it.toDto() })
    }
}
