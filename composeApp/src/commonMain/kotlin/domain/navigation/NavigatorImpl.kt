package domain.navigation

import domain.navigation.model.Destination
import domain.navigation.model.Destination.HomeDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

object NavigatorImpl : Navigator {
    private val destinations: LinkedList<Destination> = LinkedList<Destination>().apply { add(HomeDestination) }

    private val _currentDestination: MutableStateFlow<Destination> = MutableStateFlow(destinations.last())
    override val currentDestination: StateFlow<Destination> = _currentDestination

    override val canNavigateBack: MutableStateFlow<Boolean> = MutableStateFlow(destinations.isNotEmpty())

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
        if (destinations.size < 2) return

        destinations.removeLast()
        canNavigateBack.value = destinations.isNotEmpty()
        updateCurrentDestination()
    }

    private fun updateCurrentDestination() {
        _currentDestination.value = destinations.last()
    }
}
