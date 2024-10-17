package new_structure.presentation.persona.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import new_structure.domain.persona.PersonaUseCases
import new_structure.domain.persona.state.PersonaState.*
import new_structure.presentation.persona.presenter.PersonaPresenter

@Composable
fun PersonaSelectorStateMachine(
    personaUseCase: PersonaUseCases = PersonaUseCases,
    personaPresenter: PersonaPresenter = PersonaPresenter(),
    onDismiss: () -> Unit,
) {
    val state by personaUseCase.state.collectAsState()

    LaunchedEffect(state) {
        when (val localState = state) {
            is Init -> localState.onInit()
            is PersonaSelector -> Unit
            is AddPersona -> Unit
            is Dismiss -> {
                onDismiss()
                localState.afterDismiss()
            }
        }
    }

    when (val localState = state) {
        is Init -> Unit

        is PersonaSelector -> PersonaSelectorDialog(
            personas = localState.personas?.map { personaPresenter.getPersonaItem(it) },
            onAddPersona = localState.onAddPersona,
            onClearPersona = localState.onClearPersona,
            onEditPersona = localState.onEditPersona,
            onDeletePersona = localState.onDeletePersona,
            onSelectPersona = localState.onSelectPersona,
            onDismiss = localState.onDismiss,
        )

        is AddPersona -> AddPersonaDialog(
            newPersonaItem = personaPresenter.getNewPersonaItem(localState.newPersona),
            isEdit = localState.isEdit,
            onChangeName = localState.onUpdateName,
            onChangeInstructions = localState.onUpdateInstructions,
            onSubmit = localState.onSubmit,
            onDismiss = localState.onDismiss,
        )

        is Dismiss -> Unit
    }
}
