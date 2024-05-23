package new_structure.domain.conversation.data

import kotlinx.coroutines.flow.Flow
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.data.model.PersonaData

interface ConversationRepository {
    fun getReplyStream(conversation: ConversationData): Flow<String>

    suspend fun getPersonas(): List<PersonaData>

    suspend fun addPersona(persona: PersonaData)

    suspend fun deletePersona(id: String)
}
