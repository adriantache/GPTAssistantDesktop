package new_structure.presentation.newConversation

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
import new_structure.presentation.newConversation.model.NewConversationItem
import new_structure.presentation.newConversation.view.MessageView
import new_structure.presentation.newConversation.view.PromptInput
import org.jetbrains.compose.resources.painterResource
import theme.AppColor

@Composable
fun NewConversationScreen(newConversationItem: NewConversationItem) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            modifier = Modifier.requiredHeight(36.dp),
            onClick = newConversationItem.onSelectPersona,
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

                Text(newConversationItem.selectedPersona)
            }
        }

        Spacer(Modifier.height(16.dp))

        Spacer(Modifier.weight(1f))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
        ) {
            itemsIndexed(
                items = newConversationItem.messages,
                key = { _, message -> message.id }
            ) { index, message ->
                MessageView(
                    message = message,
                    isLoading = newConversationItem.isLoading && index == newConversationItem.messages.size - 1,
                )
            }

            item {
                PromptInput(
                    prompt = newConversationItem.input,
                    onPromptChanged = newConversationItem.onInput,
                    isLoading = newConversationItem.isLoading,
                    onSubmit = newConversationItem.onSubmit,
                )
            }
        }
    }
}
