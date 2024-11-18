package domain.home.state

import domain.home.ui.model.HomeUi

sealed interface HomeState {
    data class Init(val onInit: () -> Unit) : HomeState

    data class DisplayHome(val home: HomeUi) : HomeState
}
