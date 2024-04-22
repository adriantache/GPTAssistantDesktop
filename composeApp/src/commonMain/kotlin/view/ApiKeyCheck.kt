package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import settings.AppSettings
import theme.AppColor

// Used because flow collecting requires a non-normal initial value and we can't use null here.
private const val DUMMY_VALUE = "DUMMY_VALUE"

@Composable
fun ApiKeyCheck(
    settings: AppSettings = AppSettings.getInstance(),
) {
    if (settings.apiKeyFlow.collectAsState(DUMMY_VALUE).value.isNullOrBlank()) {
        CardColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            var apiKey by remember { mutableStateOf("") }

            Text(
                text = "Your OpenAI API key is missing, please enter it below!",
                color = AppColor.onCard(),
            )

            Spacer(Modifier.height(8.dp))

            // TODO: extract this component
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = apiKey,
                onValueChange = { apiKey = it },
                trailingIcon = {
                    TextButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = {
                            if (apiKey.isBlank()) return@TextButton

                            settings.setApiKey(apiKey)
                        },
                    ) {
                        Text(
                            text = "Save",
                            color = AppColor.onCard(),
                        )
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = AppColor.onCard(),
                    placeholderColor = AppColor.onCard(),
                    focusedLabelColor = AppColor.onCard(),
                    focusedIndicatorColor = AppColor.onCard(),
                    unfocusedLabelColor = AppColor.onCard().copy(0.7f),
                    unfocusedIndicatorColor = AppColor.onCard().copy(0.7f),
                    cursorColor = AppColor.onCard(),
                ),
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}
