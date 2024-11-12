package new_structure.data.settings.dataSource

import new_structure.domain.settings.data.model.SettingsData

interface SettingsDataSource {
    suspend fun getSettings(): SettingsData

    suspend fun setApiKey(key: String?)
}
