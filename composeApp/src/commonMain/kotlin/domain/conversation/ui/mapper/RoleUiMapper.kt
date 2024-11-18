package domain.conversation.ui.mapper

import domain.conversation.entity.Role
import domain.conversation.entity.Role.*
import domain.conversation.ui.model.RoleUi

fun Role.toUi(): RoleUi = when (this) {
    USER -> RoleUi.USER
    ASSISTANT -> RoleUi.ASSISTANT
    SYSTEM -> RoleUi.SYSTEM
}
