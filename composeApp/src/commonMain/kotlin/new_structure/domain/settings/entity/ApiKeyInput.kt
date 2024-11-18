package new_structure.domain.settings.entity

import androidx.annotation.CheckResult

data class ApiKeyInput(
    val input: String = "",
) {
    val isValid = input.isNotBlank() && input.startsWith("sk-")

    @CheckResult
    fun onInput(input: String): ApiKeyInput {
        return this.copy(
            input = input.trim() // trimming automatically since key is usually copy pasted from somewhere
        )
    }
}
