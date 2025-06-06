package data.settings.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import data.dataStore.DataStoreHelper
import domain.settings.data.model.SettingsData
import kotlinx.coroutines.flow.firstOrNull

private val API_KEY_KEY = stringPreferencesKey("API_KEY_KEY_NEW")
private val TTS_VOICE_KEY = stringPreferencesKey("TTS_VOICE_KEY")

class SettingsDataSourceImpl(
    private val store: DataStore<Preferences> = DataStoreHelper.instance,
) : SettingsDataSource {
    override suspend fun getSettings(): SettingsData {
        val apiKey = store.data.firstOrNull()?.get(API_KEY_KEY)
        val ttsVoice = store.data.firstOrNull()?.get(TTS_VOICE_KEY)

        return SettingsData(
            apiKey = apiKey,
            ttsVoice = ttsVoice,
        )
    }

    override suspend fun setApiKey(key: String?) {
        store.updateData {
            it.toMutablePreferences().apply {
                if (key == null) {
                    remove(API_KEY_KEY)
                } else {
                    set(API_KEY_KEY, key)
                }
            }
        }
    }

    override suspend fun setTtsVoice(voiceId: String?) {
        store.updateData {
            it.toMutablePreferences().apply {
                if (voiceId == null) {
                    remove(TTS_VOICE_KEY)
                } else {
                    set(TTS_VOICE_KEY, voiceId)
                }
            }
        }
    }
}
