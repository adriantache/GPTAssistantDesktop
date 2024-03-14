package view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.OpenAiApiCaller
import api.model.ChatMessage
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()

    val client = remember { OpenAiApiCaller() }

    var response by remember { mutableStateOf(emptyList<ChatMessage>()) }

    Column(
        Modifier.fillMaxWidth().scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (false) {
            CardColumn {
                var apiKey by remember { mutableStateOf("") }

                Text("Your OpenAI API key is missing, please enter it below!")

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                )
            }

            Spacer(Modifier.height(8.dp))
        }

        CardColumn {
            var prompt by remember { mutableStateOf("") }

            Text("Please enter your prompt:")

            Spacer(Modifier.height(8.dp))

            TextField(
                value = prompt,
                onValueChange = { prompt = it },
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        val currentPrompt = prompt
                        prompt = ""
                        response = client.getReply(currentPrompt)
                    }
                }
            ) {
                Text("Ask ChatGPT")
            }
        }

        Spacer(Modifier.height(8.dp))

        if (response.isNotEmpty()) {
            CardColumn {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(response) {
                        MessageView(message = it)
                    }
                }
            }
        }
    }
}