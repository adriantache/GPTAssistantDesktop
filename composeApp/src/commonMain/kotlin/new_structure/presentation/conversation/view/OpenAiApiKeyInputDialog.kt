package com.adriantache.gptassistant.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import new_structure.domain.settings.state.SettingsState
import new_structure.util.Strings.API_KEY_MISSING_SUBTITLE
import new_structure.util.Strings.API_KEY_MISSING_TITLE
import new_structure.util.Strings.API_KEY_MISSING_WEBSITE
import new_structure.util.Strings.SAVE_BUTTON
import theme.AppColor

private const val OPENAI_API_KEY_PAGE = "https://platform.openai.com/account/api-keys"

@Composable
fun OpenAiApiKeyInputDialog(state: SettingsState.MissingApiKey) {
    val uriHandler = LocalUriHandler.current

    Box(
        modifier = Modifier.fillMaxSize().background(AppColor.background()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(AppColor.card(), RoundedCornerShape(16.dp))
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = API_KEY_MISSING_TITLE,
                style = MaterialTheme.typography.h6,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = API_KEY_MISSING_SUBTITLE,
                style = MaterialTheme.typography.subtitle1,
                fontStyle = FontStyle.Italic,
            )

            Spacer(Modifier.height(16.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.apiKey,
                enabled = true,
                onValueChange = { state.onInput(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { state.onSubmit() }),
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { uriHandler.openUri(OPENAI_API_KEY_PAGE) }
                    .background(AppColor.accent(), RoundedCornerShape(16.dp))
                    .padding(8.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = API_KEY_MISSING_WEBSITE,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Center,
                    color = AppColor.onAccent(),
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { state.onSubmit() }) {
                    Text(SAVE_BUTTON)
                }
            }
        }
    }
}
