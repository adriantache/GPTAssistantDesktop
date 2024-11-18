package domain.navigation.model

sealed interface Destination {
    data object HomeDestination : Destination

    data object ConversationHistoryDestination : Destination

    data class ConversationDestination(val conversationId: String? = null) : Destination

    data object NewImageGenerationDestination : Destination

    data object SettingsDestination : Destination
}
