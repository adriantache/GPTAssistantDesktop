package platformSpecific.tts

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import com.adriantache.ContextProvider
import data.settings.SettingsRepositoryImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platformSpecific.tts.model.TtsVoice
import java.util.*

// TODO: improve the error states
object TtsHelperImpl : TtsHelper {
    private val tts: TextToSpeech
    private val audioManager: AudioManager
    private val audioFocusRequest
        get() = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT).build()
    private val settingsRepository = SettingsRepositoryImpl() // TODO: definitely don't access this like this

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
        val expectedVoice = tts.voices.find { it.name == voice.id }

        if (expectedVoice == null) Log.e(this::class.simpleName, "Cannot find voice $voice!")
        else runBlocking { settingsRepository.setTtsVoice(voice.id) }

        tts.voice = expectedVoice ?: tts.defaultVoice
    }

    override fun getVoice(): TtsVoice? {
        return tts.voice?.let { getTtsVoice(it) }
    }

    override fun getVoices(): List<TtsVoice> {
        return tts.voices.orEmpty()
            .sortedBy { it.name.lowercase() }
            .map { getTtsVoice(it) }
            .filter { it.locale?.language == "en" }
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

            runBlocking {  // TODO: really improve this part...
                val settings = settingsRepository.getSettings()
                if (settings.ttsVoice != null) {
                    voice = voices.find { it.name == settings.ttsVoice }
                } else {
                    setLanguage(Locale.US).apply {
                        if (this in listOf(TextToSpeech.LANG_MISSING_DATA, TextToSpeech.LANG_NOT_SUPPORTED)) {
                            Log.e("TTS", "This Language is not supported")
                        }
                    }
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

    private fun getTtsVoice(voice: Voice): TtsVoice {
        val sections = voice.name.split("-")
        val locale = Locale(
            sections[0],
            sections[1]
        )
        val description = sections.drop(2)
            .filter { it.length > 1 }
            .mapIndexed { index, string ->
                if (index != 0) return@mapIndexed string

                string.mapIndexed { index, char ->
                    if (index == 0) char.uppercase() else char
                }
            }
            .joinToString(" ")
        val name = "${locale.displayName} $description"

        return TtsVoice(
            id = voice.name,
            name = name,
            locale = locale,
            quality = voice.quality,
        )
    }
}
