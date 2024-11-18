package domain.persona.ui.mapper

import domain.persona.model.NewPersona
import domain.persona.ui.model.NewPersonaUi

fun NewPersona.toUi() = NewPersonaUi(
    name = name,
    instructions = instructions,
    canSubmit = isValid,
)
