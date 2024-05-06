package new_structure.domain.conversation.data

import kotlinx.coroutines.flow.Flow
import new_structure.domain.conversation.data.model.ConversationData

interface ConversationRepository {
    fun getReplyStream(conversation: ConversationData): Flow<String>
}