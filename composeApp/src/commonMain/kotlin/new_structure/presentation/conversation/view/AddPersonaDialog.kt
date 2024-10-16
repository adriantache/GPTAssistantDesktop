package new_structure.presentation.conversation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import new_structure.domain.addPersona.AddPersonaUseCase
import new_structure.domain.addPersona.state.AddPersonaState
import new_structure.util.Strings.ADD_PERSONA_DESCRIPTION_LABEL
import new_structure.util.Strings.ADD_PERSONA_NAME_LABEL
import new_structure.util.Strings.ADD_PERSONA_SAVE
import new_structure.util.Strings.ADD_PERSONA_TITLE
import theme.AppColor

@Composable
fun AddPersonaDialog(
    addPersonaUseCase: AddPersonaUseCase = AddPersonaUseCase,
    onDismiss: () -> Unit,
) {
    val state by addPersonaUseCase.state.collectAsState()

    LaunchedEffect(state) {
        (state as? AddPersonaState.Init)?.onInit?.invoke()
    }

    (state as? AddPersonaState.AddPersona)?.let { localState ->
        Dialog(onDismissRequest = onDismiss) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(AppColor.card(), RoundedCornerShape(8.dp)).padding(16.dp),
            ) {
                Text(
                    text = ADD_PERSONA_TITLE,
                    color = AppColor.onCard(),
                    style = MaterialTheme.typography.subtitle1,
                )

                TextField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    value = localState.name,
                    onValueChange = localState.onChangeName,
                    placeholder = {
                        Text(ADD_PERSONA_NAME_LABEL)
                    },
                    colors = AppColor.textFieldColors(),
                )

                TextField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    value = localState.description,
                    onValueChange = localState.onChangeDescription,
                    placeholder = {
                        Text(ADD_PERSONA_DESCRIPTION_LABEL)
                    },
                    colors = AppColor.textFieldColors(),
                )

                Button(
                    enabled = localState.canSubmit,
                    onClick = {
                        localState.onSubmit()
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
}
