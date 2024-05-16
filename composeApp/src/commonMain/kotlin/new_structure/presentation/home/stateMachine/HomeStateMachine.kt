package new_structure.presentation.home.stateMachine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import new_structure.domain.home.HomeUseCases
import new_structure.domain.home.state.HomeState.DisplayHome
import new_structure.domain.home.state.HomeState.Init
import new_structure.presentation.home.HomeScreen
import new_structure.presentation.home.presenter.HomePresenter

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
