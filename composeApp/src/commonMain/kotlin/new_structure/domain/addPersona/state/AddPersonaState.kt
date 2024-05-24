package new_structure.domain.addPersona.state

sealed interface AddPersonaState {
    data class Init(val onInit: () -> Unit) : AddPersonaState

    data class AddPersona(
        val name: String,
        val description: String,
        val onChangeName: (String) -> Unit,
        val onChangeDescription: (String) -> Unit,
        val canSubmit: Boolean,
        val onSubmit: () -> Unit,
    ) : AddPersonaState
}
