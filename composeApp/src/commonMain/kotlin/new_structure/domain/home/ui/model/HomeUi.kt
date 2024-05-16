package new_structure.domain.home.ui.model

data class HomeUi(
    val destinations: List<HomeDestinationUi>,
    val onClick: (HomeDestinationUi) -> Unit,
)
