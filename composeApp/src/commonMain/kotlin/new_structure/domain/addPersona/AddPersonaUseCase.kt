package new_structure.domain.addPersona

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import new_structure.data.ConversationRepositoryImpl
import new_structure.domain.addPersona.model.NewPersona
import new_structure.domain.addPersona.state.AddPersonaState
import new_structure.domain.conversation.data.ConversationRepository
import new_structure.domain.conversation.data.mapper.toData

class AddPersonaUseCase(
    private val repository: ConversationRepository = ConversationRepositoryImpl(),
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {
    private val _state: MutableStateFlow<AddPersonaState> = MutableStateFlow(AddPersonaState.Init(::onInit))
    val state: StateFlow<AddPersonaState> = _state

    private var newPersona = NewPersona()

    private fun onInit() {
        updateState()
    }

    private fun onChangeName(name: String) {
        newPersona = newPersona.onChangeName(name)
        updateState()
    }

    private fun onChangeDescription(description: String) {
        newPersona = newPersona.onChangeDescription(description)
        updateState()
    }

    private fun updateState() {
        _state.value = AddPersonaState.AddPersona(
            name = newPersona.name,
            description = newPersona.description,
            onChangeName = ::onChangeName,
            onChangeDescription = ::onChangeDescription,
            onSubmit = ::onSubmit,
        )
    }

    private fun onSubmit() {
        val persona = newPersona.onSubmit() ?: return

        scope.launch {
            repository.addPersona(persona.toData())
        }
    }
}
