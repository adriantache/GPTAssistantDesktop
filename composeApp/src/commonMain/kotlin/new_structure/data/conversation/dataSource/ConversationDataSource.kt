package new_structure.data.conversation.dataSource

import kotlinx.coroutines.flow.Flow
import new_structure.data.conversation.dataSource.mapper.toDto
import new_structure.data.conversation.dataSource.service.StreamingApiCaller
import new_structure.data.conversation.dataSource.service.StreamingApiCallerImpl
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.util.model.Outcome

class ConversationDataSource(
    private val apiConfig: StreamingApiCaller = StreamingApiCallerImpl(),
) {
    fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>> {
        return apiConfig.getReply(conversation.messages.values.map { it.toDto() })
    }
}
