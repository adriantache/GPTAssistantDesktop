package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import api.model.Persona
import settings.AppSettings
import theme.AppColor

@Composable
fun AddPersonaDialog(
    appSettings: AppSettings,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        var personaName by remember { mutableStateOf("") }
        var personaInstructions by remember { mutableStateOf("") }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(AppColor.card(), RoundedCornerShape(8.dp)).padding(16.dp),
        ) {
            Text(
                text = "Add persona",
                color = AppColor.onCard(),
                style = MaterialTheme.typography.subtitle1,
            )

            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                value = personaName,
                onValueChange = { personaName = it },
                placeholder = {
                    Text("Persona name")
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = AppColor.onCard(),
                    placeholderColor = AppColor.onCard(),
                    focusedLabelColor = AppColor.onCard(),
                    focusedIndicatorColor = AppColor.onCard(),
                    unfocusedLabelColor = AppColor.onCard().copy(0.7f),
                    unfocusedIndicatorColor = AppColor.onCard().copy(0.7f),
                    cursorColor = AppColor.onCard(),
                )
            )

            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                value = personaInstructions,
                onValueChange = { personaInstructions = it },
                placeholder = {
                    Text("Persona instructions")
                },
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

            Button(
                onClick = {
                    appSettings.personas += Persona(
                        name = personaName,
                        instructions = personaInstructions,
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppColor.userMessage(),
                    contentColor = AppColor.onUserMessage(),
                )
            ) {
                Text("Save")
            }
        }
    }
}
