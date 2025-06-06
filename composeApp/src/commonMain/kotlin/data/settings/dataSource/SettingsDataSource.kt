package data.settings.dataSource

import domain.settings.data.model.SettingsData

interface SettingsDataSource {
    suspend fun getSettings(): SettingsData

    suspend fun setApiKey(key: String?)

    suspend fun setTtsVoice(voiceId: String?)
}
