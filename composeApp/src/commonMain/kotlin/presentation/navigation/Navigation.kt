package presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import domain.navigation.Navigator
import domain.navigation.NavigatorImpl
import domain.navigation.model.Destination.*
import domain.settings.SettingsUseCases
import domain.settings.state.SettingsState
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.settings
import platformSpecific.BackHandlerHelper
import presentation.conversation.stateMachine.ConversationStateMachine
import presentation.conversation.view.OpenAiApiKeyInputDialog
import presentation.conversationHistory.ConversationHistoryStateMachine
import presentation.home.stateMachine.HomeStateMachine
import presentation.imageGeneration.stateMachine.ImageGenerationStateMachine
import presentation.navigation.view.SettingsScreen
import presentation.shared.CloseRow
import presentation.shared.CloseRowItem

@Composable
fun Navigation(
    navigator: Navigator = NavigatorImpl,
    settingsUseCases: SettingsUseCases = SettingsUseCases,
) {
    val currentDestination by navigator.currentDestination.collectAsState()

    BackHandlerHelper(isActive = navigator.canNavigateBack.collectAsState().value) {
        navigator.navigateBack()
    }

    // TODO: implement settings wrapper for API key input
    val settingsState by settingsUseCases.state.collectAsState()

    Column(Modifier.fillMaxSize()) {
        SettingsWrapper(settingsState) {
            CloseRow(
                extraItems = (settingsState as? SettingsState.Idle)?.let {
                    listOf(
                        CloseRowItem(
                            imageRes = Res.drawable.settings,
                            contentDescription = "Settings",
                            onClick = { it.onDisplaySettings() }

                        )
                    )
                }
            ) {
                navigator.navigateBack()
            }

            when (val destination = currentDestination) {
                HomeDestination -> HomeStateMachine()
                is ConversationDestination -> ConversationStateMachine(destination.conversationId)
                NewImageGenerationDestination -> ImageGenerationStateMachine()
                ConversationHistoryDestination -> ConversationHistoryStateMachine()
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
        is SettingsState.MissingApiKey -> OpenAiApiKeyInputDialog(settingsState)
        is SettingsState.SettingsVisible -> SettingsScreen(settingsState)
        is SettingsState.Idle -> content()
    }
}
