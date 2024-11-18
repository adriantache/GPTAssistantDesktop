package presentation.home.stateMachine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import domain.home.HomeUseCases
import domain.home.state.HomeState.DisplayHome
import domain.home.state.HomeState.Init
import presentation.home.HomeScreen
import presentation.home.presenter.HomePresenter

@Composable
fun HomeStateMachine(
    homeUseCases: HomeUseCases = HomeUseCases(),
    homePresenter: HomePresenter = HomePresenter(),
) {
    val state by homeUseCases.state.collectAsState()

    LaunchedEffect(state) {
        when (val localState = state) {
            is DisplayHome -> Unit
            is Init -> localState.onInit()
        }
    }

    when (val localState = state) {
        is DisplayHome -> HomeScreen(homePresenter.getHomeItem(localState.home))
        is Init -> Unit
    }
}
