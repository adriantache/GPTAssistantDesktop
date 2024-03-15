package settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

private const val API_KEY_KEY = "API_KEY_KEY"

class AppSettings private constructor() {
    private val settings: Settings = Settings()

    var apiKey: String? = null
        get() = settings.getStringOrNull(API_KEY_KEY)
        set(value) {
            settings[API_KEY_KEY] = value
            field = value
        }

    companion object {
        private val INSTANCE = AppSettings()

        fun getInstance(): AppSettings {
            return INSTANCE
        }
    }
}