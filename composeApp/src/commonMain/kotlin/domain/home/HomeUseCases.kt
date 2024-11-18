package domain.home

import domain.home.entity.HomeDestination
import domain.home.entity.HomeDestination.*
import domain.home.state.HomeState
import domain.home.state.HomeState.Init
import domain.home.ui.mapper.toEntity
import domain.home.ui.mapper.toUi
import domain.home.ui.model.HomeDestinationUi
import domain.home.ui.model.HomeUi
import domain.navigation.Navigator
import domain.navigation.NavigatorImpl
import domain.navigation.model.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
            NEW_CONVERSATION -> navigator.navigateTo(Destination.ConversationDestination())
            CONVERSATION_HISTORY -> navigator.navigateTo(Destination.ConversationHistoryDestination)
            NEW_IMAGE_GENERATION -> navigator.navigateTo(Destination.NewImageGenerationDestination)
        }
    }
}
