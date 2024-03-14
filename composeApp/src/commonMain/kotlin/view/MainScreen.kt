package view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    Column(
        Modifier.fillMaxWidth().scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (true) {
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
                onClick = { TODO() }
            ) {
                Text("Ask ChatGPT")
            }
        }

        Spacer(Modifier.height(8.dp))

        Card {
            Text("response")
        }
    }
}