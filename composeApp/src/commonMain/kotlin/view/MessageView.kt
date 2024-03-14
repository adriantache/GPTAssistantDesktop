package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.model.ChatMessage
import api.model.ChatRole

@Composable
fun MessageView(
    modifier: Modifier = Modifier,
    message: ChatMessage,
) {
    val bgColor = when (message.role) {
        ChatRole.user -> MaterialTheme.colors.primary
        else -> MaterialTheme.colors.secondary
    }
    val textColor =
        if (message.role == ChatRole.user) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary
    val contentAlignment = if (message.role == ChatRole.user) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = contentAlignment,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = modifier.fillMaxWidth(0.8f)
                .background(bgColor, RoundedCornerShape(8.dp))
                .padding(12.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message.content,
                color = textColor,
            )
        }
    }
}
