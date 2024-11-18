package data.settings

import data.settings.dataSource.SettingsDataSource
import data.settings.dataSource.SettingsDataSourceImpl
import domain.settings.data.SettingsRepository
import domain.settings.data.model.SettingsData

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
