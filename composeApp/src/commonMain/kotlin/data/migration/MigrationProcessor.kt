package data.migration

import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import data.conversationHistory.dataSource.ConversationHistoryDataSourceImpl
import data.dataStore.DataStoreHelper
import data.migration.legacy.AppSettingsImpl
import data.migration.legacy.ChatRole
import data.migration.legacy.Storage
import data.persona.dataSource.PersonaDataSource
import data.settings.dataSource.SettingsDataSourceImpl
import domain.conversation.data.model.ConversationData
import domain.conversation.data.model.MessageData
import domain.conversation.data.model.RoleData
import domain.persona.data.model.PersonaData
import kotlinx.coroutines.flow.firstOrNull
import java.util.*

private val MIGRATIONS_KEY = intPreferencesKey("MIGRATIONS_KEY")
private const val EXPECTED_MIGRATION = 5

@Composable
fun MigrationProcessor(content: @Composable () -> Unit) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        runMigrations {
            showContent = true
        }
    }

    if (showContent) {
        content()
    }
}

private suspend fun runMigrations(
    dataStore: DataStore<Preferences> = DataStoreHelper.instance,
    onMigrationsDone: () -> Unit,
) {
    val preferences = dataStore.data.firstOrNull()
    val migration = preferences?.get(MIGRATIONS_KEY) ?: 0

    if (migration == EXPECTED_MIGRATION) {
        onMigrationsDone()
        return
    }

    for (i in migration..EXPECTED_MIGRATION) {
        runMigration(migration = i, dataStore = dataStore)
    }

    dataStore.updateData {
        it.toMutablePreferences().apply {
            set(MIGRATIONS_KEY, EXPECTED_MIGRATION)
        }
    }
    onMigrationsDone()
}

private suspend fun runMigration(
    migration: Int,
    dataStore: DataStore<Preferences>,
) {
    when (migration) {
        4 -> migrateToNewStructure(dataStore)
    }
}

// todo remove this on Migration 5
suspend fun migrateToNewStructure(
    dataStore: DataStore<Preferences>,
) {
    migrateApiKey(dataStore)
    migrateConversations(dataStore)
    migratePersonas(dataStore)

    val darkModeKey = booleanPreferencesKey("FORCE_DARK_MODE_KEY")
    dataStore.updateData {
        it.toMutablePreferences().apply {
            remove(darkModeKey)
        }
    }
}

suspend fun migrateApiKey(
    dataStore: DataStore<Preferences>,
    appSettings: AppSettingsImpl = AppSettingsImpl,
) {
    val apiKey = appSettings.apiKeyFlow.firstOrNull() ?: return
    val settings = SettingsDataSourceImpl(dataStore)

    settings.setApiKey(apiKey)
    appSettings.clearApiKey()
}

suspend fun migrateConversations(
    dataStore: DataStore<Preferences>,
    storage: Storage = Storage.getInstance(),
) {
    val conversations = storage.cacheFlow.firstOrNull() ?: return
    val conversationHistoryDataSource = ConversationHistoryDataSourceImpl(dataStore)

    conversations.values.forEach { conversation ->
        val conversationData = ConversationData(
            id = conversation.id,
            messages = conversation.contents.associate {
                val messageData = MessageData(
                    id = it.id,
                    content = it.content,
                    role = when (it.role) {
                        ChatRole.system -> RoleData.SYSTEM
                        ChatRole.assistant -> RoleData.ASSISTANT
                        ChatRole.user -> RoleData.USER
                    }
                )
                messageData.id to messageData
            },
            title = null,
            persona = null,
        )

        conversationHistoryDataSource.saveConversation(conversationData)
        storage.deleteConversation(conversation.id)
    }
}

suspend fun migratePersonas(
    dataStore: DataStore<Preferences>,
    appSettings: AppSettingsImpl = AppSettingsImpl,
) {
    val personas = appSettings.personasFlow.firstOrNull() ?: return
    val personasDataSource = PersonaDataSource(dataStore)

    personas.forEach {
        val persona = it.value
        val personaData = PersonaData(
            id = UUID.randomUUID().toString(),
            name = persona.name,
            instructions = persona.instructions,
        )

        personasDataSource.addPersona(personaData)
    }
}
