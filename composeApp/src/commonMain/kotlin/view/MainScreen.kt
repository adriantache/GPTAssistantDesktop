package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import api.OpenAiStreamingApiCaller
import api.model.Conversation
import kotlinx.coroutines.launch
import platformSpecific.ScrollbarImpl
import storage.Storage
import theme.AppColor

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val apiCaller = remember { OpenAiStreamingApiCaller() }
    val storage = remember { Storage() }

    var prompt by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var response: Conversation? by remember { mutableStateOf(null) }

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

                    storage.updateConversation(it)
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
            if (response == null || response?.isEmpty == true) {
                var isHistoryExpanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(AppColor.card())
                        .clickable { isHistoryExpanded = !isHistoryExpanded }
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text("Previous conversations")
                }

                Spacer(Modifier.height(16.dp))

                AnimatedVisibility(isHistoryExpanded) {
                    val cache by storage.cacheFlow.collectAsState()

                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    ) {
                        items(cache) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .clickable {
                                            apiCaller.setConversation(it)
                                            response = it
                                        }
                                        .padding(16.dp)
                                        .weight(1f),
                                    maxLines = 1,
                                    text = it.title,
                                    overflow = TextOverflow.Ellipsis,
                                )

                                Spacer(Modifier.width(8.dp))

                                Text(
                                    modifier = Modifier
                                        .clickable { storage.deleteConversation(it.id) }
                                        .padding(16.dp),
                                    text = "Delete",
                                    color = MaterialTheme.colors.error,
                                )
                            }
                        }
                    }
                }
            } else {
                Box(contentAlignment = Alignment.TopEnd) {
                    LazyColumn(
                        modifier = Modifier.padding(end = 12.dp),
                        verticalArrangement = Arrangement.Bottom,
                        state = listState,
                    ) {
                        items(response?.contents.orEmpty()) {
                            MessageView(message = it)

                            Spacer(Modifier.height(12.dp))
                        }

                        if (response?.isEmpty != true) {
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
