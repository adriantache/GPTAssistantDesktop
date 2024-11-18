package domain.home.ui.mapper

import domain.home.entity.HomeDestination
import domain.home.entity.HomeDestination.*
import domain.home.ui.model.HomeDestinationUi

fun HomeDestination.toUi() = when (this) {
    NEW_CONVERSATION -> HomeDestinationUi.NEW_CONVERSATION
    CONVERSATION_HISTORY -> HomeDestinationUi.CONVERSATION_HISTORY
    NEW_IMAGE_GENERATION -> HomeDestinationUi.NEW_IMAGE_GENERATION
}

fun HomeDestinationUi.toEntity() = when (this) {
    HomeDestinationUi.NEW_CONVERSATION -> NEW_CONVERSATION
    HomeDestinationUi.CONVERSATION_HISTORY -> CONVERSATION_HISTORY
    HomeDestinationUi.NEW_IMAGE_GENERATION -> NEW_IMAGE_GENERATION
}
