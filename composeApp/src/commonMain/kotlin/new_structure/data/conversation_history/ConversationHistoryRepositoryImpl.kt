package new_structure.data.conversation_history

import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation_history.data.ConversationHistoryRepository
import new_structure.domain.conversation_history.data.model.ConversationHistoryData
import new_structure.domain.util.model.Outcome
import java.time.LocalDateTime

class ConversationHistoryRepositoryImpl : ConversationHistoryRepository {
    override fun getConversations(
        searchText: String?,
        startDateTime: LocalDateTime?,
        endDateTime: LocalDateTime?
    ): Outcome<List<ConversationHistoryData>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteConversation(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getConversation(id: String): ConversationData {
        TODO("Not yet implemented")
    }

    override suspend fun saveConversation(conversationData: ConversationData) {
        TODO("Not yet implemented")
    }
}
