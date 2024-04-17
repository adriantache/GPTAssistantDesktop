package platformSpecific

import com.adriantache.ContextProvider

actual fun providePath(): String {
    val context = requireNotNull(ContextProvider.context.get())

    return context.filesDir.path
}
