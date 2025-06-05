package platformSpecific.tts

import kotlinx.coroutines.flow.StateFlow
import platformSpecific.tts.model.TtsVoice

interface TtsHelper {
    fun speak(text: String): StateFlow<Boolean>
    fun stop()
    fun setVoice(voice: TtsVoice)
    fun getVoice(): TtsVoice?
    fun getVoices(): List<TtsVoice>
}

expect fun getTtsHelper(): TtsHelper?
