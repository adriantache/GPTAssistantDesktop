package domain.conversation.data.model

import domain.persona.data.model.PersonaData

data class ConversationData(
    val id: String,
    val title: String?,
    val messages: Map<String, MessageData>,
    val persona: PersonaData?,
)
