package view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import api.OpenAiApiCaller
import api.model.ChatMessage
import kotlinx.coroutines.launch
import platformSpecific.ScrollbarImpl
import theme.AppColor

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val client = remember { OpenAiApiCaller() }

    var prompt by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var response by remember { mutableStateOf(emptyList<ChatMessage>()) }

    fun onSubmit() {
        if (prompt.isBlank()) return

        scope.launch {
            isLoading = true

            response = client.getReply(prompt)
            isLoading = false
            prompt = ""

            listState.scrollToItem(response.size - 2)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (false) {
            CardColumn(color = MaterialTheme.colors.error) {
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

        CardColumn(
            modifier = Modifier.weight(1f),
            color = Color.Transparent,
        ) {
            Box(
                contentAlignment = Alignment.TopEnd,
            ) {
                LazyColumn(
                    modifier = Modifier.padding(end = 12.dp),
                    verticalArrangement = Arrangement.Bottom,
                    state = listState,
                ) {
                    items(response) {
                        MessageView(message = it)

                        Spacer(Modifier.height(12.dp))
                    }
                }

                ScrollbarImpl(listState)
            }
        }

        Spacer(Modifier.height(8.dp))

        CardColumn {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = prompt,
                onValueChange = {
                    prompt = it
                },
                enabled = !isLoading,
                label = {
                    Text("Ask ChatGPT")
                },
                trailingIcon = {
                    TextButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = ::onSubmit,
                        enabled = !isLoading && prompt.isNotBlank(),
                    ) {
                        Text(
                            text = "Send",
                            style = MaterialTheme.typography.subtitle1,
                            color = AppColor.UserMessage,

                            )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
            )
        }
    }
}