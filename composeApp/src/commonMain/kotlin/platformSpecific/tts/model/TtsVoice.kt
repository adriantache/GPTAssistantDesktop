package platformSpecific.tts.model

import java.util.*

data class TtsVoice(
    val id: String,
    val name: String?,
    val locale: Locale?,
    val quality: Int,
)
