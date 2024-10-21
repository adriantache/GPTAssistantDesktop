package new_structure.data.conversation.dataSource

import kotlinx.coroutines.flow.Flow
import new_structure.data.conversation.dataSource.mapper.toDto
import new_structure.data.conversation.dataSource.service.StreamingApiCaller
import new_structure.data.conversation.dataSource.service.StreamingApiCallerImpl
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData
import new_structure.domain.util.model.Outcome

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
