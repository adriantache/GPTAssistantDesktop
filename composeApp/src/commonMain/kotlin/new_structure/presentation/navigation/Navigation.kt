package new_structure.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import new_structure.domain.navigation.Navigator
import new_structure.domain.navigation.NavigatorImpl
import new_structure.domain.navigation.model.Destination.*
import new_structure.presentation.home.stateMachine.HomeStateMachine
import new_structure.presentation.imageGeneration.stateMachine.ImageGenerationStateMachine
import new_structure.presentation.newConversation.stateMachine.NewConversationStateMachine
import platformSpecific.BackHandlerHelper

@Composable
fun Navigation(
    navigator: Navigator = NavigatorImpl,
) {
    val currentDestination by navigator.currentDestination.collectAsState()

    BackHandlerHelper(isActive = navigator.canNavigateBack) {
        navigator.navigateBack()
    }

    Column(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.fillMaxWidth().clickable {
                navigator.navigateBack()
            }.padding(8.dp),
            text = "X",
            textAlign = TextAlign.End,
        )


        when (currentDestination) {
            Home -> HomeStateMachine()
            NewConversation -> NewConversationStateMachine()
            NewImageGeneration -> ImageGenerationStateMachine()
            ConversationHistory -> TODO()
            Settings -> TODO()
        }
    }
}
