package new_structure.presentation.persona.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import new_structure.presentation.conversation.model.PersonaItem
import new_structure.util.Strings.PERSONA_SELECTOR_ADD_PERSONA
import new_structure.util.Strings.PERSONA_SELECTOR_DELETE_PERSONA
import new_structure.util.Strings.PERSONA_SELECTOR_EDIT_PERSONA
import new_structure.util.Strings.PERSONA_SELECTOR_NO_PERSONA
import new_structure.util.Strings.PERSONA_SELECTOR_TITLE
import theme.AppColor

@Composable
fun PersonaSelectorDialog(
    personas: List<PersonaItem>,
    onAddPersona: () -> Unit,
    onClearPersona: () -> Unit,
    onEditPersona: (id: String) -> Unit,
    onDeletePersona: (id: String) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColor.card(), RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    text = PERSONA_SELECTOR_TITLE,
                    style = MaterialTheme.typography.subtitle1,
                    color = AppColor.onCard(),
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClearPersona()
                            onDismiss()
                        }
                        .background(AppColor.background(), RoundedCornerShape(16.dp)).padding(16.dp),
                ) {
                    Text(
                        style = MaterialTheme.typography.body1.copy(fontStyle = FontStyle.Italic),
                        text = PERSONA_SELECTOR_NO_PERSONA,
                        color = AppColor.onCard(),
                    )
                }
            }

            items(items = personas, key = { it.id }) { persona ->
                Row(
                    modifier = Modifier.background(AppColor.background(), RoundedCornerShape(16.dp)).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier
                        .clickable {
                            persona.onSelect()
                            onDismiss()
                        }
                        .weight(1f)
                        .padding(16.dp)
                    ) {
                        Text(
                            text = persona.name,
                            style = MaterialTheme.typography.body1,
                            color = AppColor.onCard(),
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = { onEditPersona(persona.id) },
                        colors = AppColor.buttonColors(),
                    ) {
                        Text(PERSONA_SELECTOR_EDIT_PERSONA)
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = { onDeletePersona(persona.id) },
                        colors = AppColor.errorButtonColors(),
                    ) {
                        Text(PERSONA_SELECTOR_DELETE_PERSONA)
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        onAddPersona()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColor.userMessage(),
                        contentColor = AppColor.onUserMessage(),
                    )
                ) {
                    Text(text = PERSONA_SELECTOR_ADD_PERSONA)
                }
            }
        }
    }
}
