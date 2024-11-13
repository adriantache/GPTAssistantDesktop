package new_structure.data.conversationHistory.dataSource.model

import kotlinx.serialization.Serializable

@Serializable
data class ConversationDataEntry(
    val id: String,
    val title: String? = null,
    val messages: Map<String, MessageDataEntry>,
    val createdAt: Long,
    val persona: PersonaDataEntry?,
)

@Serializable
data class MessageDataEntry(
    val id: String,
    val content: String,
    val role: RoleDataEntry,
)

@Serializable
data class PersonaDataEntry(
    val id: String,
    val name: String,
    val instructions: String,
)

enum class RoleDataEntry {
    USER, ASSISTANT, SYSTEM
}
