package platformSpecific

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun dataStorePreferences(): DataStore<Preferences> {
    return createDataStoreWithDefaults(
        path = SETTINGS_PREFERENCES,
    )
}
