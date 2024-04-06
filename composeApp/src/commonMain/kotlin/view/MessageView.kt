package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import api.model.ChatMessage
import api.model.ChatRole
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.copy
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.AppColor

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MessageView(
    modifier: Modifier = Modifier,
    message: ChatMessage,
    clipboard: ClipboardManager = LocalClipboardManager.current,
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

        if (!isUserMessage) {
            Spacer(Modifier.width(16.dp))

            Image(
                modifier = Modifier.clickable { clipboard.setText(AnnotatedString(message.content)) }
                    .requiredSize(36.dp).padding(8.dp),
                painter = painterResource(Res.drawable.copy),
                contentDescription = "Copy",
                colorFilter = ColorFilter.tint(AppColor.onBackground()),
            )
        }
    }
}
