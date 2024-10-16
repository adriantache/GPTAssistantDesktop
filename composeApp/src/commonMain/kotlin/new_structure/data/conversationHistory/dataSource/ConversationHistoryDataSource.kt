package new_structure.data.conversationHistory.dataSource

import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversationHistory.data.model.ConversationHistoryData
import new_structure.domain.util.model.Outcome
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
