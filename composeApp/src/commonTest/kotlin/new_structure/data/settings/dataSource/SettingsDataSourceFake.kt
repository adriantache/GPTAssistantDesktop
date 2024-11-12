package new_structure.data.settings.dataSource

import new_structure.domain.settings.data.model.SettingsData

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
