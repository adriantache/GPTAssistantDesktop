package new_structure.data.conversationHistory.dataSource

import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversationHistory.data.model.ConversationHistoryData
import new_structure.domain.util.model.Outcome
import java.time.LocalDateTime

class ConversationHistoryDataSourceFake(
    private val conversationsOutput: Outcome<List<ConversationHistoryData>?>,
) : ConversationHistoryDataSource {
    var deletedId: String? = null
    var savedConversation: ConversationData? = null
    var conversationId: String? = null
    var searchText: String? = null
    var startDateTime: LocalDateTime? = null
    var endDateTime: LocalDateTime? = null

    override suspend fun getConversations(
        searchText: String?,
        startDateTime: LocalDateTime?,
        endDateTime: LocalDateTime?
    ): Outcome<List<ConversationHistoryData>?> {
        this.searchText = searchText
        this.startDateTime = startDateTime
        this.endDateTime = endDateTime

        return conversationsOutput
    }

    override suspend fun deleteConversation(id: String) {
        deletedId = id
    }

    override suspend fun getConversation(id: String): ConversationData {
        conversationId = id
        return savedConversation!!
    }

    override suspend fun saveConversation(conversationData: ConversationData) {
        savedConversation = conversationData
    }
}
