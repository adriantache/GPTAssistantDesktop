package new_structure.presentation.newConversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import new_structure.presentation.newConversation.model.NewConversationItem
import new_structure.presentation.newConversation.view.MessageView
import new_structure.presentation.newConversation.view.PromptInput

@Composable
fun NewConversationScreen(newConversationItem: NewConversationItem) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
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
