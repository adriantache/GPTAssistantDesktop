package new_structure.data.dataSource.conversation.model

import new_structure.data.model.DataError

sealed class OpenAiError(override val message: String) : DataError {
    // TODO: remove this error after removing old code
    data class HttpError(val code: Int, val body: String) :
        OpenAiError("Http Error: ($code) $body")

    // TODO: remove this error after removing old code
    data object NotAnEventStreamError :
        OpenAiError("Did not receive an event stream!")

    data object ApiKeyError :
        OpenAiError("Missing API key!")

    data class KtorError(val error: Exception) : OpenAiError(error.message ?: error::class.simpleName.orEmpty())
}
