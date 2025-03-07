package data.conversationHistory.dataSource.mapper

import androidx.annotation.VisibleForTesting
import data.conversationHistory.dataSource.model.ConversationDataEntry
import data.conversationHistory.dataSource.model.MessageDataEntry
import data.conversationHistory.dataSource.model.PersonaDataEntry
import data.conversationHistory.dataSource.model.RoleDataEntry
import domain.conversation.data.model.ConversationData
import domain.conversation.data.model.MessageData
import domain.conversation.data.model.RoleData
import domain.conversation.data.model.RoleData.*
import domain.conversationHistory.data.model.ConversationHistoryData
import domain.persona.data.model.PersonaData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ConversationData.toDataEntry(
    @VisibleForTesting
    createdAt: Long = Instant.now().toEpochMilli(), // Always update timestamp when saving it.
) = ConversationDataEntry(
    id = id,
    title = title,
    createdAt = createdAt,
    messages = messages.mapValues { it.value.toDataEntry() },
    persona = persona?.toDataEntry(),
)

fun MessageData.toDataEntry() = MessageDataEntry(
    id = id,
    content = content,
    role = role.toDataEntry(),
)

fun PersonaData.toDataEntry() = PersonaDataEntry(
    id = id,
    name = name,
    instructions = instructions
)

fun RoleData.toDataEntry() = when (this) {
    USER -> RoleDataEntry.USER
    ASSISTANT -> RoleDataEntry.ASSISTANT
    SYSTEM -> RoleDataEntry.SYSTEM
}

fun ConversationDataEntry.toData() = ConversationData(
    id = id,
    title = title,
    messages = messages.mapValues { it.value.toData() },
    persona = persona?.toData(),
)

fun MessageDataEntry.toData() = MessageData(
    id = id,
    content = content,
    role = role.toData(),
)

fun PersonaDataEntry.toData() = PersonaData(
    id = id,
    name = name,
    instructions = instructions
)

fun RoleDataEntry.toData() = when (this) {
    RoleDataEntry.USER -> USER
    RoleDataEntry.ASSISTANT -> ASSISTANT
    RoleDataEntry.SYSTEM -> SYSTEM
}

fun ConversationDataEntry.toHistoryData() = ConversationHistoryData(
    id = id,
    title = title,
    date = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()),
)
