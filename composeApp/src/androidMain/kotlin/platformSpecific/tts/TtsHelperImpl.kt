package platformSpecific.tts

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import android.util.Log
import com.adriantache.ContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import platformSpecific.tts.model.TtsVoice
import java.util.*

// TODO: improve the error states
object TtsHelperImpl : TtsHelper {
    private val tts: TextToSpeech
    private val audioManager: AudioManager
    private val audioFocusRequest
        get() = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT).build()

    init {
        val context = requireNotNull(ContextProvider.context.get())
        tts = getTts(context)
        audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
    }

    override fun speak(text: String): StateFlow<Boolean> {
        audioManager.requestAudioFocus(audioFocusRequest)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

        return getStatusFlow()
    }

    override fun stop() {
        tts.stop()
    }

    override fun setVoice(voice: TtsVoice) {
        val expectedVoice = tts.voices.find { it.name == voice.name }

        if (expectedVoice == null) Log.e(this::class.simpleName, "Cannot find voice $voice!")

        tts.voice = expectedVoice ?: tts.defaultVoice
    }

    override fun getVoice(): TtsVoice? {
        return tts.voice?.let { TtsVoice(it.name) }
    }

    override fun getVoices(): List<TtsVoice> {
        return tts.voices?.map { TtsVoice(it.name) }.orEmpty()
    }

    private fun getTts(context: Context): TextToSpeech {
        return TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Log.e("TTS", "Initialization Failed!")
                return@TextToSpeech
            }

            configureTts()
        }
    }

    private fun configureTts() {
        tts.apply {
            setPitch(1f)
            setSpeechRate(1f)
            setLanguage(Locale.US).apply {
                if (this in listOf(TextToSpeech.LANG_MISSING_DATA, TextToSpeech.LANG_NOT_SUPPORTED)) {
                    Log.e("TTS", "This Language is not supported")
                }
            }
        }
    }

    // Used because the normal status listener isn't working.
    private fun getStatusFlow(): StateFlow<Boolean> {
        val statusFlow = MutableStateFlow(true)

        CoroutineScope(Dispatchers.Default).launch {
            while (tts.isSpeaking) {
                delay(100)

                if (!tts.isSpeaking) {
                    audioManager.abandonAudioFocusRequest(audioFocusRequest)
                }

                statusFlow.value = tts.isSpeaking
            }
        }

        return statusFlow
    }
}
