package platformSpecific

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BackHandlerHelper(isActive: Boolean, block: () -> Unit) {
    BackHandler(enabled = isActive) {
        block()
    }
}
