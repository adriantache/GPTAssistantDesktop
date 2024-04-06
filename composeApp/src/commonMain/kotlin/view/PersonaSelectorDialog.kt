package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import api.OpenAiStreamingApiCaller
import settings.AppSettings
import theme.AppColor

@Composable
fun PersonaSelectorDialog(
    onDismiss: () -> Unit,
    apiCaller: OpenAiStreamingApiCaller,
    appSettings: AppSettings = AppSettings.getInstance(),
) {
    var showAddPersonaDialog by remember { mutableStateOf(false) }

    val personas by appSettings.personasFlow.collectAsState()

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

            items(personas) {
                Box(modifier = Modifier
                    .clickable {
                        appSettings.selectedPersona = it
                        apiCaller.reset()
                        onDismiss()
                    }
                    .padding(16.dp)
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.body1,
                        color = AppColor.onCard(),
                    )
                }
            }

            item {
                Button(
                    onClick = { showAddPersonaDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColor.userMessage(),
                        contentColor = AppColor.onUserMessage(),
                    )
                ) {
                    Text(text = "Add persona")
                }
            }
        }

        if (showAddPersonaDialog) {
            AddPersonaDialog(appSettings) { showAddPersonaDialog = false }
        }
    }
}
