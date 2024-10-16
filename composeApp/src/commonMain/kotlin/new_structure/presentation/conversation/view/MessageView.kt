package new_structure.presentation.conversation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.copy
import new_structure.presentation.conversation.LoadingTimer
import new_structure.presentation.conversation.model.MessageItem
import new_structure.presentation.conversation.model.RoleItem
import old_code.processAnnotations
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.AppColor

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MessageView(
    modifier: Modifier = Modifier,
    message: MessageItem,
    isLoading: Boolean = false,
    clipboard: ClipboardManager = LocalClipboardManager.current, //todo move to usecase method
) {
    val isUserMessage = message.role == RoleItem.USER

    val bgColor = if (isUserMessage) AppColor.userMessage() else AppColor.card()
    val textColor = if (isUserMessage) AppColor.onUserMessage() else AppColor.onCard()
    val contentAlignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = contentAlignment,
    ) {
        Column(modifier = modifier.fillMaxWidth(0.95f).background(bgColor, RoundedCornerShape(8.dp))) {
            SelectionContainer {
                val text by remember(message) {
                    derivedStateOf { message.message.processAnnotations() }
                }

                if (isLoading) {
                    LoadingTimer(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        textColor = textColor,
                    )
                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        text = text,
                        color = textColor,
                    )
                }
            }

            if (!isUserMessage) {
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .clickable { clipboard.setText(AnnotatedString(message.message)) }
                        .background(
                            AppColor.dark(),
                            RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp)
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier.requiredSize(36.dp).padding(8.dp),
                        painter = painterResource(Res.drawable.copy),
                        contentDescription = "Copy",
                        colorFilter = ColorFilter.tint(AppColor.light()),
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = "Copy",
                        color = AppColor.light(),
                    )

                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}
