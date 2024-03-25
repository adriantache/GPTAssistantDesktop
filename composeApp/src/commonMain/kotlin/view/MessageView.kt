package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.model.ChatMessage
import api.model.ChatRole
import theme.AppColor

@Composable
fun MessageView(
    modifier: Modifier = Modifier,
    message: ChatMessage,
) {
    val isUserMessage = message.role == ChatRole.user

    val bgColor = if (isUserMessage) AppColor.userMessage() else AppColor.card()
    val textColor = if (isUserMessage) AppColor.onUserMessage() else AppColor.onCard()
    val contentAlignment = if (isUserMessage) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = contentAlignment,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(0.8f),
            color = bgColor,
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    text = message.content,
                    color = textColor,
                )
            }
        }
    }
}
