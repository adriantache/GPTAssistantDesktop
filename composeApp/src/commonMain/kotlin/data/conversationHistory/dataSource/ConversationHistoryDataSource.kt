package data.conversationHistory.dataSource

import domain.conversation.data.model.ConversationData
import domain.conversationHistory.data.model.ConversationHistoryData
import domain.util.model.Outcome
import java.time.LocalDateTime

interface ConversationHistoryDataSource {
    suspend fun getConversations(
        searchText: String? = null,
        startDateTime: LocalDateTime? = null,
        endDateTime: LocalDateTime? = null,
    ): Outcome<List<ConversationHistoryData>?>

    suspend fun deleteConversation(id: String)

    suspend fun getConversation(id: String): ConversationData

    suspend fun saveConversation(conversationData: ConversationData)
}
