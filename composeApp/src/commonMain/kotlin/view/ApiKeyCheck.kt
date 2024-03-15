package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import settings.AppSettings

@Composable
fun ApiKeyCheck(
    settings: AppSettings = AppSettings.getInstance(),
) {
    var shouldShowApiKeyInput by remember { mutableStateOf(settings.apiKey.isNullOrBlank()) }

    if (shouldShowApiKeyInput) {
        CardColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            var apiKey by remember { mutableStateOf("") }

            Text("Your OpenAI API key is missing, please enter it below!")

            Spacer(Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = apiKey,
                onValueChange = { apiKey = it },
                trailingIcon = {
                    TextButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = {
                            if (apiKey.isBlank()) return@TextButton

                            settings.apiKey = apiKey
                            shouldShowApiKeyInput = false
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}