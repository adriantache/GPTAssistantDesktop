package new_structure.domain.navigation

import kotlinx.coroutines.flow.StateFlow
import new_structure.domain.navigation.model.Destination


interface Navigator {
    val currentDestination: StateFlow<Destination>
    val canNavigateBack: Boolean

    fun navigateTo(destination: Destination)

    fun navigateBack()
}
