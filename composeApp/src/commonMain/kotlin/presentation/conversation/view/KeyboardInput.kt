package presentation.conversation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import theme.AppColor
import util.Strings.INPUT_ENTER_TO_SEND
import util.Strings.INPUT_HINT
import util.Strings.SEND_BUTTON

@Composable
fun KeyboardInput(
    prompt: String,
    onPromptChanged: (input: String) -> Unit,
    isLoading: Boolean,
    onSubmit: (isVoiceInput: Boolean) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxWidth()) {
        var singleLine by remember { mutableStateOf(true) }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        TextField(
            modifier = Modifier.fillMaxWidth()
                .focusRequester(focusRequester),
            value = prompt,
            onValueChange = { onPromptChanged(it) },
            enabled = !isLoading,
            label = {
                Text(
                    text = INPUT_HINT,
                    color = AppColor.onCard(),
                )
            },
            trailingIcon = {
                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = {
                        keyboard?.hide()
                        onSubmit(false)
                    },
                    enabled = !isLoading && prompt.isNotBlank(),
                ) {
                    Text(
                        text = SEND_BUTTON,
                        style = MaterialTheme.typography.subtitle1,
                        color = AppColor.userMessage(),
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboard?.hide()
                onSubmit(false)
            }),
            singleLine = singleLine,
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
            maxLines = if (singleLine) 1 else 10,
        )

        Spacer(Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = singleLine,
                onCheckedChange = { singleLine = it },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = AppColor.onCard(),
                    checkedColor = AppColor.userMessage(),
                )
            )

            Text(
                modifier = Modifier.clickable { singleLine = !singleLine },
                text = INPUT_ENTER_TO_SEND,
                color = AppColor.onCard()
            )
        }
    }
}
