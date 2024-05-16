package new_structure.data

import kotlinx.coroutines.flow.Flow
import new_structure.data.dataSource.ConversationDataSource
import new_structure.domain.conversation.data.ConversationRepository
import new_structure.domain.conversation.data.model.ConversationData

class ConversationRepositoryImpl(
    private val dataSource: ConversationDataSource = ConversationDataSource(),
) : ConversationRepository {
    override fun getReplyStream(conversation: ConversationData): Flow<String> {
        return dataSource.getReplyStream(conversation)
    }
}
