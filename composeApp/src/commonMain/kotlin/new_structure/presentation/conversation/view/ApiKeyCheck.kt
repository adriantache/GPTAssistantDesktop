package new_structure.presentation.conversation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import new_structure.domain.settings.state.SettingsState
import old_code.view.CardColumn
import theme.AppColor

@Composable
fun ApiKeyCheck(state: SettingsState.MissingApiKey) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppColor.background(),
    ) {
        CardColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Your OpenAI API key is missing, please enter it below!",
                color = AppColor.onCard(),
            )

            Spacer(Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.apiKey,
                onValueChange = state.onInput,
                trailingIcon = {
                    TextButton(
                        enabled = state.canSubmit,
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = state.onSubmit,
                    ) {
                        Text(
                            text = "Save",
                            color = AppColor.onCard(),
                        )
                    }
                },
                singleLine = true,
                colors = AppColor.textFieldColors(),
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}
