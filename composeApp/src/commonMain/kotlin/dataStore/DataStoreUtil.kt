package dataStore

import androidx.datastore.preferences.core.Preferences
import kotlinx.serialization.json.Json

inline fun <reified T> Preferences.decodeJson(
    key: Preferences.Key<String>,
    json: Json = Json,
): T? {
    val string = this[key] ?: return null

    return json.decodeFromString<T>(string)
}
