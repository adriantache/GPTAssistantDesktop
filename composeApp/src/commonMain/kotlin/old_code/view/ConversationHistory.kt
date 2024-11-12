package old_code.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import new_structure.data.migration.legacy.Conversation
import new_structure.data.migration.legacy.Storage
import theme.AppColor

@Composable
fun ColumnScope.ConversationHistory(
    storage: Storage,
    onSetConversation: (Conversation) -> Unit,
) {
    var isHistoryExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
            .background(AppColor.card())
            .clickable { isHistoryExpanded = !isHistoryExpanded }
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = "Previous conversations",
            color = AppColor.onCard(),
        )
    }

    Spacer(Modifier.height(16.dp))

    AnimatedVisibility(isHistoryExpanded) {
        val cache by storage.cacheFlow.collectAsState(emptyMap())

        LazyColumn(
            modifier = Modifier.background(AppColor.card()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier
                            .clickable {

                            }
                            .padding(16.dp)
                            .weight(1f),
                        maxLines = 1,
                        text = "title",
                        overflow = TextOverflow.Ellipsis,
                        color = AppColor.onCard(),
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        modifier = Modifier
                            .clickable { }
                            .padding(16.dp),
                        text = "Delete",
                        color = MaterialTheme.colors.error,
                    )
                }
            }
        }
    }
}
