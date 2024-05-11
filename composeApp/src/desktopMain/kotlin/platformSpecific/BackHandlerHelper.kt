package platformSpecific

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandlerHelper(block: () -> Unit) = Unit // Stub, behaviour only makes sense on mobile.
