package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
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
import api.OpenAiStreamingApiCaller
import api.model.ChatMessage
import kotlinx.coroutines.launch
import platformSpecific.ScrollbarImpl
import theme.AppColor

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val apiCaller = remember { OpenAiStreamingApiCaller() }

    var prompt by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var response by remember { mutableStateOf(emptyList<ChatMessage>()) }

    fun onSubmit() {
        if (prompt.isBlank()) return

        scope.launch {
            isLoading = true

            isLoading = false
            val localPrompt = prompt
            prompt = ""

            apiCaller.getReply(localPrompt).collect {
                response = it

                scope.launch {
                    listState.animateScrollBy(999f) // Scroll to the end of the list to match the streaming.
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ApiKeyCheck()

        SettingsRow()

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
            PromptInput(
                prompt = prompt,
                onPromptChanged = { prompt = it },
                isLoading = isLoading,
                onSubmit = ::onSubmit,
            )
        }
    }
}

@Composable
fun PromptInput(
    prompt: String,
    onPromptChanged: (String) -> Unit,
    isLoading: Boolean,
    onSubmit: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        var singleLine by remember { mutableStateOf(true) }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = prompt,
            onValueChange = onPromptChanged,
            enabled = !isLoading,
            label = {
                Text(
                    text = "Ask ChatGPT",
                    color = AppColor.onCard(),
                )
            },
            trailingIcon = {
                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = onSubmit,
                    enabled = !isLoading && prompt.isNotBlank(),
                ) {
                    Text(
                        text = "Send",
                        style = MaterialTheme.typography.subtitle1,
                        color = AppColor.userMessage(),
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            singleLine = singleLine,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = AppColor.onCard(),
                placeholderColor = AppColor.onCard(),
                focusedLabelColor = AppColor.onCard(),
                focusedIndicatorColor = AppColor.onCard(),
                unfocusedLabelColor = AppColor.onCard().copy(0.7f),
                unfocusedIndicatorColor = AppColor.onCard().copy(0.7f),
                cursorColor = AppColor.onCard(),
            ),
        )

        Spacer(Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = singleLine,
                onCheckedChange = { singleLine = it },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = AppColor.onCard(),
                    checkedColor = AppColor.userMessage(),
                )
            )

            Text(
                modifier = Modifier.clickable { singleLine = !singleLine },
                text = "Press enter to send",
                color = AppColor.onCard()
            )
        }
    }
}