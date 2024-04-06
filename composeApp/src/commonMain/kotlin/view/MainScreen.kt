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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

        SettingsRow(apiCaller)

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

                    if (response.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .clickable { response = apiCaller.reset() }
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "Reset conversation",
                                    style = MaterialTheme.typography.button,
                                    color = AppColor.onBackground(),
                                )
                            }
                        }
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
