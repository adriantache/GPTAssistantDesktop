package domain.persona

import data.persona.PersonaRepositoryImpl
import domain.conversation.ConversationUseCases
import domain.conversation.data.mapper.toData
import domain.conversation.data.mapper.toEntity
import domain.conversation.entity.Persona
import domain.conversation.ui.mapper.toUi
import domain.persona.data.PersonaRepository
import domain.persona.model.NewPersona
import domain.persona.state.PersonaState
import domain.persona.ui.mapper.toUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object PersonaUseCases {
    private val repository: PersonaRepository = PersonaRepositoryImpl()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val conversationUseCases = ConversationUseCases

    private val _state: MutableStateFlow<PersonaState> = MutableStateFlow(PersonaState.Init(::onInit))
    val state: StateFlow<PersonaState> = _state

    private var personas: List<Persona>? = null
    private var newPersona: NewPersona = NewPersona()
    private var selectedPersona: Persona? = null

    private fun onInit() {
        updatePersonas()
    }

    private fun updatePersonas() {
        scope.launch {
            personas = repository.getPersonas().values.map { it.toEntity() }
            _state.value = PersonaState.PersonaSelector(
                personas = personas?.map { it.toUi() },
                onAddPersona = ::onAddPersona,
                onClearPersona = ::onClearPersona,
                onEditPersona = ::onEditPersona,
                onSelectPersona = ::onSelectPersona,
                onDeletePersona = ::onDeletePersona,
                onDismiss = ::onDismissPersonaSelector,
            )
        }
    }

    private fun onDeletePersona(id: String) {
        scope.launch {
            repository.deletePersona(id)
            updatePersonas()

            // Ensure that if we delete the selected persona, it doesn't stay selected.
            if (selectedPersona?.id == id) onClearPersona()
        }
    }

    private fun onEditPersona(id: String) {
        // TODO: improve this behaviour
        val persona = personas?.find { it.id == id } ?: return

        newPersona = NewPersona(persona)
        updateAddPersonaState(isEdit = true) // Using the same flow as for add.
    }

    private fun onSelectPersona(id: String) {
        // TODO: improve this behaviour
        selectedPersona = personas?.find { it.id == id } ?: return

        conversationUseCases.onSetPersona(selectedPersona)
        onDismissPersonaSelector()
    }

    private fun onClearPersona() {
        selectedPersona = null

        conversationUseCases.onSetPersona(null)
        onDismissPersonaSelector()
    }

    private fun onDismissPersonaSelector() {
        _state.value = PersonaState.Dismiss {
            _state.value = PersonaState.Init(::onInit)
        }
    }

    // region add persona
    private fun onAddPersona() {
        newPersona = NewPersona()
        updateAddPersonaState()
    }

    private fun updateAddPersonaState(isEdit: Boolean = false) {
        _state.value = PersonaState.AddPersona(
            newPersona = newPersona.toUi(),
            isEdit = isEdit,
            onUpdateName = ::onUpdateNewName,
            onUpdateInstructions = ::onUpdateNewInstructions,
            onSubmit = ::onSubmitNewPersona,
            onDismiss = ::onDismissAddPersona,
        )
    }

    private fun onUpdateNewName(input: String) {
        newPersona = newPersona.onChangeName(input)
        updateAddPersonaState()
    }

    private fun onUpdateNewInstructions(input: String) {
        newPersona = newPersona.onChangeInstructions(input)
        updateAddPersonaState()
    }

    private fun onSubmitNewPersona() {
        newPersona.onSubmit()?.let {
            scope.launch {
                repository.addPersona(it.toData())
                conversationUseCases.onSetPersona(it)
                updatePersonas()
            }
        }
    }

    private fun onDismissAddPersona() {
        updatePersonas()
    }
    //endregion
}
