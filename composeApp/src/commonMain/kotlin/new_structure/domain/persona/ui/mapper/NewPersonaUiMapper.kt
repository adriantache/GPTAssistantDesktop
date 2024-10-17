package new_structure.domain.persona.ui.mapper

import new_structure.domain.persona.model.NewPersona
import new_structure.domain.persona.ui.model.NewPersonaUi

fun NewPersona.toUi() = NewPersonaUi(
    name = name,
    instructions = instructions,
    canSubmit = isValid,
)
