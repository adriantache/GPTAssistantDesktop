package new_structure.data.conversation.dataSource

import kotlinx.coroutines.flow.Flow
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.util.model.Outcome

class ConversationDataSourceFake(
    private val expectedFlow: Flow<Outcome<String>>,
) : ConversationDataSource {
    override fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>> {
        return expectedFlow
    }
}
