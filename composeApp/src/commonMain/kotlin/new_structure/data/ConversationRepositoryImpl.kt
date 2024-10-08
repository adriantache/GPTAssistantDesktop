package new_structure.data

import kotlinx.coroutines.flow.Flow
import new_structure.data.dataSource.conversation.ConversationDataSource
import new_structure.data.dataSource.persona.PersonaDataSource
import new_structure.domain.conversation.data.ConversationRepository
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.data.model.PersonaData
import new_structure.domain.util.model.Outcome

class ConversationRepositoryImpl(
    private val conversationDataSource: ConversationDataSource = ConversationDataSource(),
    private val personaDataSource: PersonaDataSource = PersonaDataSource(),
) : ConversationRepository {
    override fun getReplyStream(conversation: ConversationData): Flow<Outcome<String>> {
        return conversationDataSource.getReplyStream(conversation)
    }

    override suspend fun getPersonas(): List<PersonaData> {
        return personaDataSource.getPersonas()
    }

    override suspend fun addPersona(persona: PersonaData) {
        personaDataSource.addPersona(persona)
    }

    override suspend fun deletePersona(id: String) {
        personaDataSource.deletePersona(id)
    }
}
