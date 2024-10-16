package new_structure.domain.conversation.data.mapper

import new_structure.domain.conversation.data.model.RoleData
import new_structure.domain.conversation.entity.Role
import new_structure.domain.conversation.entity.Role.*

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
