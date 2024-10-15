package old_code.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import old_code.api.model.Conversation
import platformSpecific.ScrollbarImpl
import theme.AppColor

@Composable
fun ConversationView(
    listState: LazyListState,
    conversation: Conversation?,
    onResetConversation: () -> Unit,
) {
    if (conversation == null || conversation.isEmpty) return

    Box(contentAlignment = Alignment.TopEnd) {
        LazyColumn(
            modifier = Modifier.padding(end = 12.dp), // Used for the scrollbar.
            verticalArrangement = Arrangement.Bottom,
            state = listState,
        ) {
            itemsIndexed(
                items = conversation.contents,
                key = { _, message -> message.id },
            ) { index, message ->
                MessageView(message = message)

                if (index != conversation.contents.size - 1) {
                    Spacer(Modifier.height(12.dp))
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

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

        ScrollbarImpl(listState)
    }
}
