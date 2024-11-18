package data.error

import domain.util.model.Error

sealed class DataError(override val message: String) : Error

sealed class OpenAiError(override val message: String) : DataError(message) {
    data object ApiKeyError :
        OpenAiError("Missing API key!")

    data class KtorError(val error: Exception) : OpenAiError(error.message ?: error::class.simpleName.orEmpty())
}
