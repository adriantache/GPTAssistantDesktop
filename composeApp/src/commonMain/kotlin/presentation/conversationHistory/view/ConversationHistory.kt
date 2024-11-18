package presentation.conversationHistory.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.conversationHistory.ui.model.ConversationHistoryUi
import presentation.shared.ConfirmationDialog
import theme.AppColor
import util.Strings.CONVERSATION_HISTORY_DELETE_TEXT
import util.Strings.CONVERSATION_HISTORY_DELETE_TITLE
import util.Strings.DELETE_BUTTON

@Composable
fun ConversationHistory(
    conversation: ConversationHistoryUi,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier
                .clickable { onSelect() }
                .padding(16.dp)
                .weight(1f),
            maxLines = 2,
            text = conversation.title ?: conversation.date.toString(),
            overflow = TextOverflow.Ellipsis,
            color = AppColor.onCard(),
        )

        Spacer(Modifier.width(8.dp))

        Text(
            modifier = Modifier
                .clickable { showConfirmationDialog = true }
                .padding(16.dp),
            text = DELETE_BUTTON,
            color = MaterialTheme.colors.error,
        )
    }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            onDismiss = { showConfirmationDialog = false },
            onConfirm = { onDelete() },
            title = CONVERSATION_HISTORY_DELETE_TITLE,
            text = CONVERSATION_HISTORY_DELETE_TEXT,
        )
    }
}
