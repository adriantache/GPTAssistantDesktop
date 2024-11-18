package data.conversation.dataSource

import domain.conversation.data.model.ConversationData
import domain.util.model.Outcome
import kotlinx.coroutines.flow.Flow

class ConversationDataSourceFake(
    private val expectedFlow: Flow<Outcome<String>>,
) : ConversationDataSource {
    override fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>> {
        return expectedFlow
    }
}
