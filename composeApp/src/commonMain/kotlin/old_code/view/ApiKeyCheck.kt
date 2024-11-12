package old_code.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.AppColor

@Composable
fun ApiKeyCheck(
) {
    val apiKey = ""

    if (apiKey.isNullOrBlank()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppColor.background(),
        ) {
            CardColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                var apiKeyInput by remember { mutableStateOf("") }

                Text(
                    text = "Your OpenAI API key is missing, please enter it below!",
                    color = AppColor.onCard(),
                )

                Spacer(Modifier.height(8.dp))

                // TODO: extract this component
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = apiKeyInput,
                    onValueChange = { apiKeyInput = it },
                    trailingIcon = {
                        TextButton(
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = {
                                if (apiKeyInput.isBlank()) return@TextButton

                            },
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
}
