package new_structure.presentation.home.model

data class HomeItem(
    val name: String,
    val onClick: () -> Unit,
)
