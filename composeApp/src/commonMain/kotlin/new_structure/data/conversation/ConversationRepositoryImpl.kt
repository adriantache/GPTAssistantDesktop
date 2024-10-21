package new_structure.data.conversation

import kotlinx.coroutines.flow.Flow
import new_structure.data.conversation.dataSource.ConversationDataSource
import new_structure.data.conversation.dataSource.ConversationDataSourceImpl
import new_structure.domain.conversation.data.ConversationRepository
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.util.model.Outcome

class ConversationRepositoryImpl(
    // TODO: move to DI
    private val conversationDataSource: ConversationDataSource = ConversationDataSourceImpl(),
) : ConversationRepository {
    override fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>> {
        return conversationDataSource.getReplyStream(conversation)
    }
}
