package new_structure.domain.navigation

import new_structure.domain.navigation.model.Destination


interface Navigator {
    fun navigateTo(destination: Destination)

    fun navigateBack(): Boolean
}
