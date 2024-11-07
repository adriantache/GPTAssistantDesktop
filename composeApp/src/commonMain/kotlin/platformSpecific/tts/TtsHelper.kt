package platformSpecific.tts

import kotlinx.coroutines.flow.StateFlow

interface TtsHelper {
    fun speak(text: String): StateFlow<Boolean>
    fun stop()
}

expect fun getTtsHelper(): TtsHelper?
