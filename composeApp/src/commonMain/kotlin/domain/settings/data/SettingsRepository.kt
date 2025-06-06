package domain.settings.data

import domain.settings.data.model.SettingsData

interface SettingsRepository {
    suspend fun getSettings(): SettingsData

    suspend fun setApiKey(key: String?)

    suspend fun setTtsVoice(voiceId: String?)
}
