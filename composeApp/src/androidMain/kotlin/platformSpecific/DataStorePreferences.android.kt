package platformSpecific

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.adriantache.ContextProvider
import java.io.File

actual fun dataStorePreferences(): DataStore<Preferences> {
    val context = requireNotNull(ContextProvider.context.get())

    return createDataStoreWithDefaults(
        path = { fileName ->
            File(context.filesDir, "datastore/$fileName").path
        },
    )
}