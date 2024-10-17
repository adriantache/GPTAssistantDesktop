package new_structure.domain.conversation.data.model

import new_structure.domain.persona.data.model.PersonaData

data class ConversationData(
    val id: String,
    val messages: Map<String, MessageData>,
    val persona: PersonaData?,
)
