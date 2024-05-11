package platformSpecific

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandlerHelper(block: () -> Unit)
