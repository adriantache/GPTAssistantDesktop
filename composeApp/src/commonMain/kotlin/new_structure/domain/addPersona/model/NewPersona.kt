package new_structure.domain.addPersona.model

import new_structure.domain.conversation.entity.Persona

data class NewPersona(
    val name: String = "",
    val description: String = "",
) {
    val isValid = name.isNotBlank() && description.isNotBlank()

    fun onChangeName(name: String): NewPersona {
        return this.copy(name = name)
    }

    fun onChangeDescription(description: String): NewPersona {
        return this.copy(description = description)
    }

    fun onSubmit(): Persona? {
        if (!isValid) return null

        return Persona(
            name = name,
            instructions = description,
        )
    }
}
