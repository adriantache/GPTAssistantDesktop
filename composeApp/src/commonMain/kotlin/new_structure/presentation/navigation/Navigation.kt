package new_structure.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import new_structure.domain.navigation.Navigator
import new_structure.domain.navigation.NavigatorImpl
import new_structure.domain.navigation.model.Destination.*
import new_structure.presentation.home.stateMachine.HomeStateMachine
import platformSpecific.BackHandlerHelper

@Composable
fun Navigation(
    navigator: Navigator = NavigatorImpl,
) {
    val currentDestination by navigator.currentDestination.collectAsState()

    // TODO: add solution for Desktop, probably just an X
    BackHandlerHelper(isActive = navigator.canNavigateBack) {
        navigator.navigateBack()
    }

    when (currentDestination) {
        Home -> HomeStateMachine()
        NewConversation -> TODO()
        NewImageGeneration -> TODO()
        ConversationHistory -> TODO()
        Settings -> TODO()
    }
}
