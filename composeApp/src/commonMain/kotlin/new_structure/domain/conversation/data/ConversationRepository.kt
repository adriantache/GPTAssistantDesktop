package new_structure.domain.conversation.data

import kotlinx.coroutines.flow.Flow
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.util.model.Outcome

interface ConversationRepository {
    fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>>
}
