package new_structure.domain.home.ui.mapper

import new_structure.domain.home.entity.HomeDestination
import new_structure.domain.home.entity.HomeDestination.*
import new_structure.domain.home.ui.model.HomeDestinationUi

fun HomeDestination.toUi() = when (this) {
    NEW_CONVERSATION -> HomeDestinationUi.NEW_CONVERSATION
    CONVERSATION_HISTORY -> HomeDestinationUi.CONVERSATION_HISTORY
    NEW_IMAGE_GENERATION -> HomeDestinationUi.NEW_IMAGE_GENERATION
}
