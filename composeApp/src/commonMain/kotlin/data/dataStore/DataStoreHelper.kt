package data.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import platformSpecific.dataStorePreferences

// TODO: migrate to DI
object DataStoreHelper {
    @get:Synchronized
    val instance: DataStore<Preferences> = dataStorePreferences()
}
