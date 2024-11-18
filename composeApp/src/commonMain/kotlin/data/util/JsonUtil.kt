package data.util

import androidx.datastore.preferences.core.Preferences
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

inline fun <reified T> Preferences.decodeJson(
    key: Preferences.Key<String>,
    json: Json = Json,
): T? {
    val string = this[key] ?: return null

    return string.decodeJson(json)
}

inline fun <reified T> String?.decodeJson(json: Json = Json): T? {
    if (this == null) return null

    return try {
        json.decodeFromString<T>(this)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    } catch (e: SerializationException) {
        e.printStackTrace()
        null
    }
}
