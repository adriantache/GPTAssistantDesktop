package new_structure.domain.navigation.model

sealed interface Destination {
    data object Home : Destination

    data object PreviousConversations : Destination

    data object NewConversation : Destination

    data object NewImageGeneration : Destination
}
