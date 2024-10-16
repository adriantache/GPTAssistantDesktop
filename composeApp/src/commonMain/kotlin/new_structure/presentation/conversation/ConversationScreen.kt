package new_structure.presentation.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.persona
import new_structure.presentation.conversation.model.ConversationItem
import new_structure.presentation.conversation.view.MessageView
import new_structure.presentation.conversation.view.PromptInput
import org.jetbrains.compose.resources.painterResource
import theme.AppColor

@Composable
fun NewConversationScreen(conversationItem: ConversationItem) {
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
        ) {
            itemsIndexed(
                items = conversationItem.messages,
                key = { _, message -> message.id }
            ) { index, message ->
                MessageView(
                    message = message,
                    isLoading = conversationItem.isLoading && index == conversationItem.messages.size - 1,
                )
            }

            item {
                PromptInput(
                    prompt = conversationItem.input,
                    onPromptChanged = conversationItem.onInput,
                    isLoading = conversationItem.isLoading,
                    onSubmit = conversationItem.onSubmit,
                )
            }
        }
    }
}
