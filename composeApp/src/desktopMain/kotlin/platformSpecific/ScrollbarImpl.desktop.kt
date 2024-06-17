package platformSpecific

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable

@Composable
actual fun ScrollbarImpl(state: LazyListState) {
    VerticalScrollbar(ScrollbarAdapter(state))
}
