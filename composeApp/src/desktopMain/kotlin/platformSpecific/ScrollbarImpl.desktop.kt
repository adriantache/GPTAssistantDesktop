package platformSpecific

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable

@Composable
actual fun ScrollbarImpl(state: LazyListState) {
    VerticalScrollbar(rememberScrollbarAdapter(state))
}
