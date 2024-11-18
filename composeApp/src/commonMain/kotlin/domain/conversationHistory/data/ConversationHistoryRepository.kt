package domain.conversationHistory.data

import domain.conversation.data.model.ConversationData
import domain.conversationHistory.data.model.ConversationHistoryData
import domain.util.model.Outcome
import java.time.LocalDateTime

interface ConversationHistoryRepository {
    suspend fun getConversations(
        searchText: String? = null,
        startDateTime: LocalDateTime? = null,
        endDateTime: LocalDateTime? = null,
    ): Outcome<List<ConversationHistoryData>?>

    suspend fun deleteConversation(id: String)

    // Doesn't return an Outcome, since we should have already checked that it exists at this point.
    suspend fun getConversation(id: String): ConversationData

    suspend fun saveConversation(conversationData: ConversationData)
}
