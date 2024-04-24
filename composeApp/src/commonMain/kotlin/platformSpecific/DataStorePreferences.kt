package platformSpecific

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath

expect fun dataStorePreferences(): DataStore<Preferences>

private const val PREFERENCES_FILE = "settings_preferences.preferences_pb"

internal fun createDataStoreWithDefaults(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    migrations: List<DataMigration<Preferences>> = emptyList(),
    path: (fileName: String) -> String,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = corruptionHandler,
        scope = coroutineScope,
        migrations = migrations,
        produceFile = { path(PREFERENCES_FILE).toPath() },
    )
}
