package new_structure.presentation.persona.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import new_structure.presentation.persona.model.NewPersonaItem
import new_structure.presentation.shared.CloseRow
import new_structure.util.Strings.ADD_PERSONA_DESCRIPTION_LABEL
import new_structure.util.Strings.ADD_PERSONA_NAME_LABEL
import new_structure.util.Strings.ADD_PERSONA_SAVE
import new_structure.util.Strings.ADD_PERSONA_TITLE
import new_structure.util.Strings.EDIT_PERSONA_TITLE
import theme.AppColor

@Composable
fun AddPersonaDialog(
    newPersonaItem: NewPersonaItem,
    isEdit: Boolean,
    onChangeName: (String) -> Unit,
    onChangeInstructions: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(AppColor.card(), RoundedCornerShape(8.dp)).padding(16.dp),
        ) {
            CloseRow { onDismiss() }

            val title = if (isEdit) EDIT_PERSONA_TITLE else ADD_PERSONA_TITLE
            Text(
                text = title,
                color = AppColor.onCard(),
                style = MaterialTheme.typography.subtitle1,
            )

            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                value = newPersonaItem.name,
                onValueChange = onChangeName,
                label = {
                    Text(ADD_PERSONA_NAME_LABEL)
                },
                colors = AppColor.textFieldColors(),
            )

            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                value = newPersonaItem.instructions,
                onValueChange = onChangeInstructions,
                label = {
                    Text(ADD_PERSONA_DESCRIPTION_LABEL)
                },
                colors = AppColor.textFieldColors(),
            )

            Button(
                enabled = newPersonaItem.canSubmit,
                onClick = {
                    onSubmit()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppColor.userMessage(),
                    contentColor = AppColor.onUserMessage(),
                )
            ) {
                Text(ADD_PERSONA_SAVE)
            }
        }
    }
}
