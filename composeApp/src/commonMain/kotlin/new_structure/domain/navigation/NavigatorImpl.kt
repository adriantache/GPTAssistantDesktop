package new_structure.domain.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import new_structure.domain.navigation.model.Destination
import new_structure.domain.navigation.model.Destination.Home
import java.util.*

object NavigatorImpl : Navigator {
    private val destinations: LinkedList<Destination> = LinkedList<Destination>().apply { add(Home) }

    private val _currentDestination: MutableStateFlow<Destination> = MutableStateFlow(destinations.last())
    override val currentDestination: StateFlow<Destination> = _currentDestination

    override val canNavigateBack: Boolean
        get() = destinations.isNotEmpty()

    override fun navigateTo(destination: Destination) {
        // Don't allow navigating multiple times to the same destination. Should probably remove entire subgraph
        // or just let people handle this manually. So:
        // TODO: reconsider this behaviour
        if (destinations.contains(destination)) {
            destinations.remove(destination)
        }

        destinations.add(destination)
        updateCurrentDestination()
    }

    override fun navigateBack() {
        if (destinations.isEmpty()) return

        destinations.removeLast()
        updateCurrentDestination()
    }

    private fun updateCurrentDestination() {
        _currentDestination.value = destinations.last()
    }
}
