package new_structure.domain.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import new_structure.domain.home.entity.HomeDestination
import new_structure.domain.home.entity.HomeDestination.*
import new_structure.domain.home.state.HomeState
import new_structure.domain.home.state.HomeState.Init
import new_structure.domain.home.ui.mapper.toEntity
import new_structure.domain.home.ui.mapper.toUi
import new_structure.domain.home.ui.model.HomeDestinationUi
import new_structure.domain.home.ui.model.HomeUi
import new_structure.domain.navigation.Navigator
import new_structure.domain.navigation.NavigatorImpl
import new_structure.domain.navigation.model.Destination

class HomeUseCases(
    private val navigator: Navigator = NavigatorImpl,
) {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(Init(::onInit))
    val state: StateFlow<HomeState> = _state

    private fun onInit() {
        updateHome()
    }

    private fun updateHome() {
        val destinations = HomeDestination.entries.map { it.toUi() }

        _state.value = HomeState.DisplayHome(HomeUi(destinations, ::onChooseDestination))
    }

    private fun onChooseDestination(destination: HomeDestinationUi) {
        when (destination.toEntity()) {
            NEW_CONVERSATION -> navigator.navigateTo(Destination.NewConversation)
            CONVERSATION_HISTORY -> navigator.navigateTo(Destination.ConversationHistory)
            NEW_IMAGE_GENERATION -> navigator.navigateTo(Destination.NewImageGeneration)
        }
    }
}
