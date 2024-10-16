package new_structure.data.conversationHistory.dataSource.model

import kotlinx.serialization.Serializable

@Serializable
data class ConversationDataEntry(
    val id: String,
    val messages: Map<String, MessageDataEntry>,
    val createdAt: Long,
)

@Serializable
data class MessageDataEntry(
    val id: String,
    val content: String,
    val role: RoleDataEntry,
)

enum class RoleDataEntry {
    USER, ASSISTANT, SYSTEM
}
