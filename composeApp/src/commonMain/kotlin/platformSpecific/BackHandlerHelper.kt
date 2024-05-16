package platformSpecific

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandlerHelper(isActive: Boolean = true, block: () -> Unit)
