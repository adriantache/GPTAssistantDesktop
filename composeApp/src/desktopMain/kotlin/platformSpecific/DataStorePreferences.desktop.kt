package platformSpecific

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import net.harawata.appdirs.AppDirs
import net.harawata.appdirs.AppDirsFactory
import java.io.File

actual fun dataStorePreferences(): DataStore<Preferences> {
    val appDirs: AppDirs = AppDirsFactory.getInstance()
    val path = appDirs.getUserDataDir("GPTAssistant", null, null)

    return createDataStoreWithDefaults(
        produceFile = { File("$path//$it") },
    )
}
