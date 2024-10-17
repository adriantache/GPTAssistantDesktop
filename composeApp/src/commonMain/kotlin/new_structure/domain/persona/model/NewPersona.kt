package new_structure.domain.persona.model

import androidx.annotation.CheckResult
import new_structure.domain.conversation.entity.Persona
import java.util.*

data class NewPersona(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val instructions: String = "",
) {
    constructor(persona: Persona) : this(
        id = persona.id,
        name = persona.name,
        instructions = persona.instructions,
    )

    val isValid = name.isNotBlank() && instructions.isNotBlank()

    @CheckResult
    fun onChangeName(name: String): NewPersona {
        return this.copy(name = name)
    }

    @CheckResult
    fun onChangeInstructions(instructions: String): NewPersona {
        return this.copy(instructions = instructions)
    }

    @CheckResult
    fun onSubmit(): Persona? {
        if (!isValid) return null

        return Persona(
            id = id,
            name = name,
            instructions = instructions,
        )
    }
}
