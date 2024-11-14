package new_structure.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import new_structure.domain.navigation.Navigator
import new_structure.domain.navigation.NavigatorImpl
import new_structure.domain.navigation.model.Destination.*
import new_structure.domain.settings.SettingsUseCases
import new_structure.domain.settings.state.SettingsState
import new_structure.presentation.conversation.stateMachine.ConversationStateMachine
import new_structure.presentation.conversation.view.ApiKeyCheck
import new_structure.presentation.conversationHistory.ConversationHistoryStateMachine
import new_structure.presentation.home.stateMachine.HomeStateMachine
import new_structure.presentation.imageGeneration.stateMachine.ImageGenerationStateMachine
import new_structure.presentation.shared.CloseRow
import platformSpecific.BackHandlerHelper

@Composable
fun Navigation(
    navigator: Navigator = NavigatorImpl,
    settingsUseCases: SettingsUseCases = SettingsUseCases,
) {
    val currentDestination by navigator.currentDestination.collectAsState()

    BackHandlerHelper(isActive = navigator.canNavigateBack) {
        navigator.navigateBack()
    }

    // TODO: implement settings wrapper for API key input
    val settingsState by settingsUseCases.state.collectAsState()

    Column(Modifier.fillMaxSize()) {
        SettingsWrapper(settingsState) {
            CloseRow { navigator.navigateBack() }

            when (val destination = currentDestination) {
                HomeDestination -> HomeStateMachine()
                is ConversationDestination -> ConversationStateMachine(destination.conversationId)
                NewImageGenerationDestination -> ImageGenerationStateMachine()
                ConversationHistoryDestination -> ConversationHistoryStateMachine()
                SettingsDestination -> TODO()
            }
        }
    }
}

@Composable
fun SettingsWrapper(
    settingsState: SettingsState,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(settingsState) {
        when (settingsState) {
            is SettingsState.Init -> settingsState.onInit()
            is SettingsState.Idle -> Unit
            is SettingsState.MissingApiKey -> Unit
            is SettingsState.SettingsVisible -> Unit
        }
    }

    when (settingsState) {
        is SettingsState.Init -> Unit
        is SettingsState.MissingApiKey -> ApiKeyCheck(settingsState)
        is SettingsState.SettingsVisible -> TODO()
        is SettingsState.Idle -> content()
    }
}
