package data.conversation.dataSource

import data.conversation.dataSource.mapper.toDto
import data.conversation.dataSource.service.StreamingApiCaller
import data.conversation.dataSource.service.StreamingApiCallerImpl
import domain.conversation.data.model.ConversationData
import domain.conversation.data.model.MessageData
import domain.conversation.data.model.RoleData
import domain.util.model.Outcome
import kotlinx.coroutines.flow.Flow

class ConversationDataSourceImpl(
    private val apiConfig: StreamingApiCaller = StreamingApiCallerImpl(),
) : ConversationDataSource {
    override fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>> {
        val messages = if (conversation.persona == null) {
            conversation.messages.values
        } else {
            val personaMessage = listOf(
                MessageData(
                    id = conversation.persona.id,
                    content = conversation.persona.instructions,
                    role = RoleData.SYSTEM,
                )
            )

            personaMessage + conversation.messages.values
        }

        return apiConfig.getReply(messages.map { it.toDto() })
    }
}
