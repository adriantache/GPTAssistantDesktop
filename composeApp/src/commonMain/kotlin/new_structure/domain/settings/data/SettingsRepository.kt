package new_structure.domain.settings.data

import new_structure.domain.settings.data.model.SettingsData

interface SettingsRepository {
    suspend fun getSettings(): SettingsData

    suspend fun setApiKey(key: String?)
}
