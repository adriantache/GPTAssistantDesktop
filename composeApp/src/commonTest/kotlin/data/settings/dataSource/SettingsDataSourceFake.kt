package data.settings.dataSource

import domain.settings.data.model.SettingsData

class SettingsDataSourceFake(
    private var settings: SettingsData = SettingsData(apiKey = null),
) : SettingsDataSource {
    override suspend fun getSettings(): SettingsData {
        return settings
    }

    override suspend fun setApiKey(key: String?) {
        settings = settings.copy(
            apiKey = key
        )
    }
}
