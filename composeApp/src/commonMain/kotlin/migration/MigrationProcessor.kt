package migration

import androidx.compose.runtime.*
import api.model.Conversation
import api.model.Persona
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import storage.Storage

private const val MIGRATIONS_KEY = "MIGRATIONS_KEY"
private const val EXPECTED_MIGRATION = 2

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

private suspend fun runMigration(
    migration: Int,
    settings: Settings,
    json: Json = Json { encodeDefaults = true },
) {
    // TODO: remove legacy data
    when (migration) {
        1 -> migratePersonas(settings, json)
        2 -> migrateConversationHistory(settings, json)
    }
}

fun migratePersonas(
    settings: Settings,
    json: Json
) {
    @Suppress("LocalVariableName")
    val PERSONAS_KEY = "PERSONAS_KEY"
    val personasString = settings.getStringOrNull(PERSONAS_KEY) ?: return

    try {
        val personas = json.decodeFromString<List<Persona>>(personasString)
        val personasMap = personas.associateBy { it.name }

        settings[PERSONAS_KEY] = json.encodeToString(personasMap)
    } catch (_: IllegalArgumentException) {
    }
}

suspend fun migrateConversationHistory(
    settings: Settings,
    json: Json,
    storage: Storage = Storage.getInstance(),
) {
    val string = settings.getStringOrNull("CONVERSATIONS_CACHE_KEY") ?: return

    try {
        val conversations = json.decodeFromString<List<Conversation>>(string)

        conversations.forEach {
            storage.updateConversation(it)
        }
    } catch (_: IllegalArgumentException) {
    }
}
