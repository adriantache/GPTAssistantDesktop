package new_structure.presentation.conversation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import platformSpecific.ScrollbarImpl

@Composable
fun ScrollbarContainer(
    listState: LazyListState,
    content: @Composable () -> Unit
) {
    Box(contentAlignment = Alignment.TopEnd) {
        content()

        ScrollbarImpl(listState)
    }
}
