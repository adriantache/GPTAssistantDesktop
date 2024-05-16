package new_structure.data.dataSource

import kotlinx.coroutines.flow.Flow
import new_structure.data.dataSource.mapper.toDto
import new_structure.domain.conversation.data.model.ConversationData

class ConversationDataSource(
    private val apiConfig: StreamingApiCaller = StreamingApiCaller(),
) {
    fun getReplyStream(conversation: ConversationData): Flow<String> {
        return apiConfig.getReply(conversation.messages.map { it.toDto() })
    }
}
