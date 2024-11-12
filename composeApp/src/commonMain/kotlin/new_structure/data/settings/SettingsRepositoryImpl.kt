package new_structure.data.settings

import new_structure.data.settings.dataSource.SettingsDataSource
import new_structure.data.settings.dataSource.SettingsDataSourceImpl
import new_structure.domain.settings.data.SettingsRepository
import new_structure.domain.settings.data.model.SettingsData

class SettingsRepositoryImpl(
    // TODO: move to DI
    private val settingsDataSource: SettingsDataSource = SettingsDataSourceImpl(),
) : SettingsRepository {
    override suspend fun getSettings(): SettingsData {
        return settingsDataSource.getSettings()
    }

    override suspend fun setApiKey(key: String?) {
        settingsDataSource.setApiKey(key)
    }
}
