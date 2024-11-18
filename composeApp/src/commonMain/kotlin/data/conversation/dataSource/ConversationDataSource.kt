package data.conversation.dataSource

import domain.conversation.data.model.ConversationData
import domain.util.model.Outcome
import kotlinx.coroutines.flow.Flow

interface ConversationDataSource {
    fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>>
}
