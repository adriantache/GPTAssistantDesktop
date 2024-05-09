package platformSpecific

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File

expect fun dataStorePreferences(): DataStore<Preferences>

private const val PREFERENCES_FILE = "settings_preferences.preferences_pb"

internal fun createDataStoreWithDefaults(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    migrations: List<DataMigration<Preferences>> = emptyList(),
    produceFile: (fileName: String) -> File,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        corruptionHandler = corruptionHandler,
        migrations = migrations,
        scope = coroutineScope,
        produceFile = { produceFile(PREFERENCES_FILE) }
    )
}
