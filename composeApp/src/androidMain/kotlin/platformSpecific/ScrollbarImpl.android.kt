package platformSpecific

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable

@Composable
actual fun ScrollbarImpl(state: LazyListState) {
    // Not yet supported by Compose on Android...
}