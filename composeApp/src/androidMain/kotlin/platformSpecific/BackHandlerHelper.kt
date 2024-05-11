package platformSpecific

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BackHandlerHelper(block: () -> Unit) {
    BackHandler {
        block()
    }
}
