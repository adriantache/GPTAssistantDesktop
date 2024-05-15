package new_structure.domain.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import new_structure.domain.home.entity.HomeDestination
import new_structure.domain.home.state.HomeState
import new_structure.domain.home.state.HomeState.Init
import new_structure.domain.home.ui.mapper.toUi
import new_structure.domain.home.ui.model.HomeUi

class HomeUseCases {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(Init(::onInit))
    val state: StateFlow<HomeState> = _state

    private fun onInit() {
        updateHome()
    }

    private fun updateHome() {
        val destinations = HomeDestination.entries.map { it.toUi() }

        _state.value = HomeState.DisplayHome(HomeUi(destinations))
    }
}
