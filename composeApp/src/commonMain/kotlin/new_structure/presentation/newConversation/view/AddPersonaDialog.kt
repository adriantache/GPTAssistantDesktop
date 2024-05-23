package new_structure.presentation.newConversation.view

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import api.model.Persona
import settings.AppSettings
import theme.AppColor

@Composable
fun AddPersonaDialog(
    persona: Persona,
    appSettings: AppSettings,
    onDismiss: () -> Unit,
) {
    val personasFlow by appSettings.personasFlow.collectAsState(emptyMap())

    Dialog(onDismissRequest = onDismiss) {
        var personaName by remember { mutableStateOf(persona.name) }
        var personaInstructions by remember { mutableStateOf(persona.instructions) }

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
                colors = AppColor.textFieldColors(),
            )

            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                value = personaInstructions,
                onValueChange = { personaInstructions = it },
                placeholder = {
                    Text("Persona instructions")
                },
                colors = AppColor.textFieldColors(),
            )

            Button(
                onClick = {
                    val personas = personasFlow.toMutableMap()

                    personas[personaName] = Persona(
                        name = personaName,
                        instructions = personaInstructions,
                    )

                    appSettings.setPersonas(personas)
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
