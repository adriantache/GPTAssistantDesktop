package platformSpecific

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import net.harawata.appdirs.AppDirs
import net.harawata.appdirs.AppDirsFactory

actual fun dataStorePreferences(): DataStore<Preferences> {
    val appDirs: AppDirs = AppDirsFactory.getInstance()
    val path = appDirs.getUserDataDir("GPTAssistant", null, null)

    println(path + SETTINGS_PREFERENCES)

    return createDataStoreWithDefaults(
        path = path + SETTINGS_PREFERENCES,
    )
}
