package old_code.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import old_code.api.OpenAiStreamingApiCaller
import old_code.api.model.Persona
import old_code.settings.AppSettings
import old_code.settings.AppSettingsImpl
import theme.AppColor

@Composable
fun PersonaSelectorDialog(
    onDismiss: () -> Unit,
    apiCaller: OpenAiStreamingApiCaller,
    appSettings: AppSettings = AppSettingsImpl,
) {
    var showAddPersonaDialog: Persona? by remember { mutableStateOf(null) }

    val personas by appSettings.personasFlow.collectAsState(emptyMap())

    Dialog(onDismissRequest = onDismiss) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColor.card(), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Select persona:",
                    style = MaterialTheme.typography.subtitle1,
                    color = AppColor.onCard(),
                )
            }

            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        appSettings.selectedPersona = null
                        apiCaller.reset()
                        onDismiss()
                    }
                    .padding(16.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.body1.copy(fontStyle = FontStyle.Italic),
                        text = "None",
                        color = AppColor.onCard(),
                    )
                }
            }

            items(personas.values.toList()) { persona ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier
                        .clickable {
                            appSettings.selectedPersona = persona
                            apiCaller.reset()
                            onDismiss()
                        }
                        .weight(1f)
                        .padding(16.dp)
                    ) {
                        Text(
                            text = persona.name,
                            style = MaterialTheme.typography.body1,
                            color = AppColor.onCard(),
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = { showAddPersonaDialog = persona },
                        colors = AppColor.buttonColors(),
                    ) {
                        Text("Edit persona")
                    }
                }
            }

            item {
                Button(
                    onClick = { showAddPersonaDialog = Persona("", "") },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColor.userMessage(),
                        contentColor = AppColor.onUserMessage(),
                    )
                ) {
                    Text(text = "Add persona")
                }
            }
        }

        showAddPersonaDialog?.let {
            AddPersonaDialog(
                persona = it,
                appSettings = appSettings,
            ) { showAddPersonaDialog = null }
        }
    }
}
