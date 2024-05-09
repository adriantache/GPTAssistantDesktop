package dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import platformSpecific.dataStorePreferences

// TODO: migrate to DI
object DataStoreHelper {
    @get:Synchronized
    @set:Synchronized
    private var instance: DataStore<Preferences>? = null

    fun getInstance(): DataStore<Preferences> {
        return instance ?: dataStorePreferences().also { instance = it }
    }
}
