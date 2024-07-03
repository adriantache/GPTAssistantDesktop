package new_structure.domain.imageGeneration.entity

data class ImagePrompt(
    val input: String = "",
) {
    val canSubmit = input.isNotBlank()

    fun onInput(input: String): ImagePrompt {
        return this.copy(input = input)
    }

    fun onSubmit(): ImagePrompt? {
        if (!canSubmit) return null

        return this.copy(input = "")
    }
}
