package new_structure.presentation.home.presenter

import new_structure.domain.home.ui.model.HomeDestinationUi.*
import new_structure.domain.home.ui.model.HomeUi
import new_structure.presentation.home.model.HomeItem
import new_structure.util.Strings

class HomePresenter {
    fun getHomeItem(ui: HomeUi): List<HomeItem> {
        return ui.destinations.map {
            val name = when (it) {
                NEW_CONVERSATION -> Strings.HOME_ITEM_NEW_CONVERSATION
                CONVERSATION_HISTORY -> Strings.HOME_ITEM_CONVERSATION_HISTORY
                NEW_IMAGE_GENERATION -> Strings.HOME_ITEM_NEW_IMAGE_GENERATION
            }

            HomeItem(
                name = name,
                onClick = { ui.onClick(it) },
            )
        }
    }
}
