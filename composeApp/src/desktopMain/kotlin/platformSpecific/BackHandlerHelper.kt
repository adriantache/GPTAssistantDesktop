package platformSpecific

import androidx.compose.runtime.Composable

@Composable
// Stub, behaviour only makes sense on mobile.
actual fun BackHandlerHelper(isActive: Boolean, block: () -> Unit) = Unit
