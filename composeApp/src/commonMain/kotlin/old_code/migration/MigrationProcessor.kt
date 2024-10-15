package old_code.migration

import androidx.compose.runtime.*
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import old_code.api.model.Conversation
import old_code.api.model.Persona
import old_code.settings.AppSettings
import old_code.settings.AppSettingsImpl
import old_code.storage.Storage

private const val MIGRATIONS_KEY = "MIGRATIONS_KEY"
private const val EXPECTED_MIGRATION = 3

// TODO: remove com.russhwolf.settings dependency in version 4 of migrations and remove all existing migration code
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

private fun runMigrations(
    settings: Settings = Settings(),
    onMigrationsDone: () -> Unit,
) {
    val migration = settings.getIntOrNull(MIGRATIONS_KEY) ?: 0

    if (migration == EXPECTED_MIGRATION) {
        onMigrationsDone()
        return
    }

    for (i in migration..EXPECTED_MIGRATION) {
        runMigration(migration = i, settings = settings)
    }

    settings[MIGRATIONS_KEY] = EXPECTED_MIGRATION
    onMigrationsDone()
}

private fun runMigration(
    migration: Int,
    settings: Settings,
    json: Json = Json { encodeDefaults = true },
) {
    // TODO: remove legacy data
    when (migration) {
        1 -> migratePersonas(settings, json)
        2 -> migrateConversationHistory(settings, json)
        3 -> migrateToDataStore(settings, json)
    }
}

fun migrateToDataStore(
    settings: Settings,
    json: Json,
    appSettings: AppSettings = AppSettingsImpl,
    storage: Storage = Storage.getInstance(),
) {
    settings.getStringOrNull("API_KEY_KEY")?.let {
        appSettings.setApiKey(it)
    }

    settings.getBooleanOrNull("FORCE_DARK_MODE_KEY")?.let {
        appSettings.setForceDarkMode(it)
    }

    settings.getStringOrNull("PERSONAS_KEY")?.let {
        val personas = try {
            json.decodeFromString<Map<String, Persona>>(it)
        } catch (_: IllegalArgumentException) {
            null
        } ?: return@let
        appSettings.setPersonas(personas)
    }

    settings.getStringOrNull("CONVERSATIONS_CACHE_KEY")?.let {
        val conversations = try {
            json.decodeFromString<Map<String, Conversation>>(it)
        } catch (_: IllegalArgumentException) {
            null
        } ?: return@let

        conversations.forEach { entry ->
            storage.updateConversation(entry.value)
        }
    }

    settings.clear()
}

fun migratePersonas(
    settings: Settings,
    json: Json
) = runCatching { // Not critical if this fails.
    val personasString = settings.getStringOrNull("PERSONAS_KEY") ?: return@runCatching

    val personas = json.decodeFromString<List<Persona>>(personasString)
    val personasMap = personas.associateBy { it.name }

    settings["PERSONAS_KEY"] = json.encodeToString(personasMap)
}

fun migrateConversationHistory(
    settings: Settings,
    json: Json,
    storage: Storage = Storage.getInstance(),
) = runCatching { // Not critical if this fails.
    val string = settings.getStringOrNull("CONVERSATIONS_CACHE_KEY") ?: return@runCatching

    val conversations = json.decodeFromString<List<Conversation>>(string)

    conversations.forEach {
        storage.updateConversation(it)
    }
}
