package new_structure.domain.conversation.data.mapper

import new_structure.domain.conversation.data.model.RoleData
import new_structure.domain.conversation.entity.Role
import new_structure.domain.conversation.entity.Role.ASSISTANT
import new_structure.domain.conversation.entity.Role.USER

fun Role.toData() = when (this) {
    USER -> RoleData.USER
    ASSISTANT -> RoleData.ASSISTANT
}