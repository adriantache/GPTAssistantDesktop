package new_structure.domain.conversation.ui.mapper

import new_structure.domain.conversation.entity.Role
import new_structure.domain.conversation.entity.Role.*
import new_structure.domain.conversation.ui.model.RoleUi

fun Role.toUi(): RoleUi = when (this) {
    USER -> RoleUi.USER
    ASSISTANT -> RoleUi.ASSISTANT
}