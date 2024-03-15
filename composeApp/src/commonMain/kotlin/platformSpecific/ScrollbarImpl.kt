package platformSpecific

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable

@Composable
expect fun ScrollbarImpl(
    state: LazyListState,
)