package new_structure.domain.conversation_history.data

import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation_history.data.model.ConversationHistoryData
import new_structure.domain.util.model.Outcome
import java.time.LocalDateTime

interface ConversationHistoryRepository {
    fun getConversations(
        searchText: String? = null,
        startDateTime: LocalDateTime? = null,
        endDateTime: LocalDateTime? = null,
    ): Outcome<List<ConversationHistoryData>>

    suspend fun deleteConversation(id: String)

    suspend fun getConversation(id: String): ConversationData

    suspend fun saveConversation(conversationData: ConversationData)
}
