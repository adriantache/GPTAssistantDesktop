package old_code.view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import new_structure.presentation.conversation.view.PromptInput
import old_code.api.OpenAiStreamingApiCaller
import old_code.api.model.Conversation
import old_code.storage.Storage
import platformSpecific.BackHandlerHelper

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val apiCaller = remember { OpenAiStreamingApiCaller() }
    val storage = remember { Storage.getInstance() }

    var prompt by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var conversation: Conversation? by remember { mutableStateOf(null) }

    fun saveConversation() {
        conversation?.let {
            // Exiting the conversation too quickly results in the coroutine being canceled,
            // so the conversation isn't saved properly.
            CoroutineScope(Dispatchers.Default).launch {
                storage.updateConversation(it)
            }
        }
    }

    fun onResetConversation() {
        saveConversation()

        conversation = apiCaller.reset()
    }

    BackHandlerHelper {
        onResetConversation()
    }

    fun onSubmit() {
        if (prompt.isBlank()) return

        scope.launch {
            isLoading = true

            isLoading = false
            val localPrompt = prompt
            prompt = ""

            apiCaller.getReply(localPrompt).collect {
                conversation = it

                scope.launch {
                    listState.scrollBy(999f) // Scroll to the end of the list to match the streaming.
                }
            }

            saveConversation()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingsRow(apiCaller)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
        ) {
            if (conversation == null || conversation?.isEmpty == true) {
                ConversationHistory(
                    storage = storage,
                    apiCaller = apiCaller,
                    onSetConversation = { conversation = it },
                )
            } else {
                ConversationView(
                    conversation = conversation,
                    listState = listState,
                    onResetConversation = ::onResetConversation,
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        CardColumn {
            PromptInput(
                prompt = prompt,
                onPromptChanged = { input, _ -> prompt = input },
                isLoading = isLoading,
                onSubmit = ::onSubmit,
            )
        }
    }

    ApiKeyCheck()
}
