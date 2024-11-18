package domain.conversation.data.mapper

import domain.conversation.data.model.RoleData
import domain.conversation.entity.Role
import domain.conversation.entity.Role.*

fun Role.toData() = when (this) {
    USER -> RoleData.USER
    ASSISTANT -> RoleData.ASSISTANT
    SYSTEM -> RoleData.SYSTEM
}

fun RoleData.toEntity() = when (this) {
    RoleData.USER -> USER
    RoleData.ASSISTANT -> ASSISTANT
    RoleData.SYSTEM -> SYSTEM
}
