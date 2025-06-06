package presentation.conversation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.persona
import org.jetbrains.compose.resources.painterResource
import platformSpecific.tts.getTtsHelper
import presentation.conversation.model.ConversationItem
import presentation.conversation.model.RoleItem
import presentation.conversation.view.*
import theme.AppColor
import util.Strings.CONVERSATION_RESET

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewConversationScreen(conversationItem: ConversationItem) {
    // TODO: add UI and functionality to stop TTS output
    val ttsHelper by remember { lazy { getTtsHelper() } }
    var isTtsSpeaking by remember { mutableStateOf(false) }

    fun onWarmUpTts() {
        // TTS takes a bit of time to get ready for operation, so we need to instantiate it before actually using it.
        @Suppress("UNUSED_EXPRESSION")
        ttsHelper
    }

    LaunchedEffect(conversationItem) {
        if (!conversationItem.isVoiceInput || ttsHelper == null) return@LaunchedEffect

        val lastMessage = conversationItem.messages.lastOrNull()

        if (lastMessage == null || lastMessage.role != RoleItem.ASSISTANT) return@LaunchedEffect

        ttsHelper?.speak(lastMessage.message)
            ?.collect { isTtsSpeaking = it }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            modifier = Modifier.requiredHeight(36.dp),
            onClick = conversationItem.onSelectPersona,
            colors = AppColor.buttonColors(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.requiredSize(24.dp),
                    painter = painterResource(Res.drawable.persona),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(AppColor.onUserMessage()),
                )

                Spacer(Modifier.width(8.dp))

                Text(conversationItem.selectedPersona)
            }
        }

        Spacer(Modifier.height(16.dp))

        Spacer(Modifier.weight(1f))

        val listState = rememberLazyListState()
        var lastMessageRole: RoleItem? by remember { mutableStateOf(null) }

        LaunchedEffect(conversationItem) {
            val lastMessage = conversationItem.messages.lastOrNull() ?: return@LaunchedEffect

            if (lastMessage.role == lastMessageRole) {
                return@LaunchedEffect
            } else {
                lastMessageRole = lastMessage.role
            }

            val scrollDestination = if (lastMessage.role == RoleItem.ASSISTANT) {
                conversationItem.messages.size - 2
            } else {
                conversationItem.messages.size - 1
            }
            listState.requestScrollToItem(scrollDestination)
        }

        ScrollbarContainer(listState) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(), // TODO migrate to animateItem() when supported.
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
            ) {
                if (isTtsSpeaking) {
                    stickyHeader {
                        AnimatedVisibility(isTtsSpeaking) {
                            StopTtsView {
                                ttsHelper?.stop()
                                isTtsSpeaking = false
                            }
                        }
                    }
                }

                itemsIndexed(
                    items = conversationItem.messages,
                    key = { _, message -> message.id }
                ) { index, message ->
                    MessageView(
                        message = message,
                        isLoading = conversationItem.isLoading && index == conversationItem.messages.size - 1,
                    )
                }

                if (conversationItem.hasMessages) {
                    item {
                        Box(
                            modifier = Modifier
                                .clickable { conversationItem.onResetConversation() }
                                .padding(16.dp),
                        ) {
                            Text(
                                text = CONVERSATION_RESET,
                                style = MaterialTheme.typography.button,
                                color = AppColor.onBackground(),
                            )
                        }
                    }
                }

                item {
                    PromptInputSelector(
                        keyboardInputContent = {
                            KeyboardInput(
                                prompt = conversationItem.input,
                                onPromptChanged = conversationItem.onInput,
                                isLoading = conversationItem.isLoading,
                                onSubmit = conversationItem.onSubmit,
                            )
                        },
                        voiceInputContent = {
                            VoiceInput(
                                onPromptChanged = conversationItem.onInput,
                                onSubmit = {
                                    onWarmUpTts()
                                    conversationItem.onSubmit(true)
                                }
                            )
                        },
                    )
                }
            }
        }
    }
}
