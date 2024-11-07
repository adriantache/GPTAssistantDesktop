package platformSpecific.tts.model

import new_structure.domain.util.model.Event

sealed interface RecognizerState {
    data object RequestingPermission : RecognizerState
    data object Ready : RecognizerState
    data class Recognizing(val amplitudePercent: Float) : RecognizerState
    data class Success(val result: Event<String>) : RecognizerState
    data object Failure : RecognizerState
}
