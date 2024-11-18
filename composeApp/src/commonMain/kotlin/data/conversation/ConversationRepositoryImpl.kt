package data.conversation

import data.conversation.dataSource.ConversationDataSource
import data.conversation.dataSource.ConversationDataSourceImpl
import domain.conversation.data.ConversationRepository
import domain.conversation.data.model.ConversationData
import domain.conversation.data.model.MessageData
import domain.conversation.data.model.RoleData
import domain.util.model.Outcome
import kotlinx.coroutines.flow.Flow

class ConversationRepositoryImpl(
    // TODO: move to DI
    private val conversationDataSource: ConversationDataSource = ConversationDataSourceImpl(),
) : ConversationRepository {
    override fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>> {
        return conversationDataSource.getReplyStream(conversation)
    }

    override suspend fun getTitle(conversation: ConversationData): String {
        val titleMessage = "titleMessage" to MessageData(
            id = "titleMessage",
            content = "Generate a title for the previous messages in this conversation. Only reply with the title itself.",
            role = RoleData.SYSTEM,
        )
        var title = ""

        conversationDataSource.getReplyStream(conversation.copy(messages = conversation.messages + titleMessage))
            .collect {
                if (it is Outcome.Success) {
                    title += it.data
                }
            }

        return title
    }
}
