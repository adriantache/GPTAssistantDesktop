package new_structure.data.conversation.dataSource

import kotlinx.coroutines.flow.Flow
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.util.model.Outcome

interface ConversationDataSource {
    fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>>
}
