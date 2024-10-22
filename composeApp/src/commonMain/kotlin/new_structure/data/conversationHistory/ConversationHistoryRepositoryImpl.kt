package new_structure.data.conversationHistory

import new_structure.data.conversationHistory.dataSource.ConversationHistoryDataSource
import new_structure.data.conversationHistory.dataSource.ConversationHistoryDataSourceImpl
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversationHistory.data.ConversationHistoryRepository
import new_structure.domain.conversationHistory.data.model.ConversationHistoryData
import new_structure.domain.util.model.Outcome
import java.time.LocalDateTime

class ConversationHistoryRepositoryImpl(
    private val conversationHistoryDataSource: ConversationHistoryDataSource = ConversationHistoryDataSourceImpl(),
) : ConversationHistoryRepository {
    override suspend fun getConversations(
        searchText: String?,
        startDateTime: LocalDateTime?,
        endDateTime: LocalDateTime?
    ): Outcome<List<ConversationHistoryData>?> {
        return conversationHistoryDataSource.getConversations(searchText, startDateTime, endDateTime)
    }

    override suspend fun deleteConversation(id: String) {
        conversationHistoryDataSource.deleteConversation(id)
    }

    override suspend fun getConversation(id: String): ConversationData {
        return conversationHistoryDataSource.getConversation(id)
    }

    override suspend fun saveConversation(conversationData: ConversationData) {
        conversationHistoryDataSource.saveConversation(conversationData)
    }
}
