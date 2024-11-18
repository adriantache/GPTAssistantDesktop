package domain.conversation.data

import domain.conversation.data.model.ConversationData
import domain.util.model.Outcome
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>>

    suspend fun getTitle(conversation: ConversationData): String
}
