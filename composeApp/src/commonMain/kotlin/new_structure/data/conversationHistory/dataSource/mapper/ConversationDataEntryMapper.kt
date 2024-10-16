package new_structure.data.conversationHistory.dataSource.mapper

import new_structure.data.conversationHistory.dataSource.model.ConversationDataEntry
import new_structure.data.conversationHistory.dataSource.model.MessageDataEntry
import new_structure.data.conversationHistory.dataSource.model.RoleDataEntry
import new_structure.domain.conversation.data.model.ConversationData
import new_structure.domain.conversation.data.model.MessageData
import new_structure.domain.conversation.data.model.RoleData
import new_structure.domain.conversation.data.model.RoleData.*
import new_structure.domain.conversationHistory.data.model.ConversationHistoryData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ConversationData.toDataEntry() = ConversationDataEntry(
    id = id,
    createdAt = Instant.now().toEpochMilli(), // Always update timestamp when saving it.
    messages = messages.mapValues { it.value.toDataEntry() },
)

fun MessageData.toDataEntry() = MessageDataEntry(
    id = id,
    content = content,
    role = role.toDataEntry(),
)

fun RoleData.toDataEntry() = when (this) {
    USER -> RoleDataEntry.USER
    ASSISTANT -> RoleDataEntry.ASSISTANT
    SYSTEM -> RoleDataEntry.SYSTEM
}

fun ConversationDataEntry.toData() = ConversationData(
    id = id,
    messages = messages.mapValues { it.value.toData() },
)

fun MessageDataEntry.toData() = MessageData(
    id = id,
    content = content,
    role = role.toData(),
)

fun RoleDataEntry.toData() = when (this) {
    RoleDataEntry.USER -> USER
    RoleDataEntry.ASSISTANT -> ASSISTANT
    RoleDataEntry.SYSTEM -> SYSTEM
}

fun ConversationDataEntry.toHistoryData() = ConversationHistoryData(
    id = id,
    title = "", // TODO: get title in UseCase and set/retrieve it here
    date = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()),
)