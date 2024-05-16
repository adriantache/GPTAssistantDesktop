package new_structure.domain.navigation.model

sealed interface Destination {
    data object Home : Destination

    data object ConversationHistory : Destination

    data object NewConversation : Destination

    data object NewImageGeneration : Destination

    data object Settings : Destination
}
