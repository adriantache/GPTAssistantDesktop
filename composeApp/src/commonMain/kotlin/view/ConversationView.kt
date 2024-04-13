package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.model.Conversation
import platformSpecific.ScrollbarImpl
import theme.AppColor

@Composable
fun ConversationView(
    listState: LazyListState,
    conversation: Conversation?,
    onResetConversation: () -> Unit,
) {
    Box(contentAlignment = Alignment.TopEnd) {
        LazyColumn(
            modifier = Modifier.padding(end = 12.dp),
            verticalArrangement = Arrangement.Bottom,
            state = listState,
        ) {
            items(conversation?.contents.orEmpty()) {
                MessageView(message = it)

                Spacer(Modifier.height(12.dp))
            }

            if (conversation?.isEmpty != true) {
                item {
                    Box(
                        modifier = Modifier
                            .clickable { onResetConversation() }
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
