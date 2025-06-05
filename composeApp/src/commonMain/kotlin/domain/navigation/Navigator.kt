package domain.navigation

import domain.navigation.model.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


interface Navigator {
    val currentDestination: StateFlow<Destination>
    val canNavigateBack: MutableStateFlow<Boolean>

    fun navigateTo(destination: Destination)

    fun navigateBack()
}
