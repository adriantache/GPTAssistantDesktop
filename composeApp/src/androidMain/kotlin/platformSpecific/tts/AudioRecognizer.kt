package platformSpecific.tts

import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import domain.util.model.Event
import platformSpecific.tts.model.RecognizerState
import platformSpecific.tts.model.RecognizerState.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun AudioRecognizer(
    shouldRecognize: Event<Boolean>,
    onStateChange: (RecognizerState) -> Unit,
    content: @Composable () -> Unit,
) {
    var state: RecognizerState by remember { mutableStateOf(Ready) }
    val context = LocalContext.current
    val microphonePermission = rememberPermissionState(RECORD_AUDIO)

    fun updateState(newState: RecognizerState) {
        val currentState = state

        val newValue = when (newState) {
            Failure -> newState.takeIf { currentState is Recognizing }
            Ready -> newState.takeIf { currentState is Success || currentState is Failure }
            is Recognizing -> newState.takeIf { currentState is Ready || currentState is Recognizing }
            is Success -> newState.takeIf { currentState is Recognizing }
            RequestingPermission -> newState
        }

        newValue?.let {
            state = it
            onStateChange(it)
        }
    }

    val recognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    updateState(Recognizing(0f))
                }

                override fun onRmsChanged(rmsdB: Float) {
                    if (state is Recognizing) {
                        updateState(Recognizing((rmsdB * 10).toInt() / 100f))
                    }
                }

                override fun onError(error: Int) {
                    val errorString = expectedErrors.getOrNull(error)
                    println("Audio Recognizer error! ($errorString)")
                    updateState(Failure)
                }

                override fun onResults(results: Bundle) {
                    val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val output = matches?.get(0) ?: ""

                    if (state is Recognizing) {
                        updateState(Success(Event(output)))
                    }
                }

                override fun onBeginningOfSpeech() = Unit
                override fun onBufferReceived(buffer: ByteArray?) = Unit
                override fun onEndOfSpeech() = Unit
                override fun onPartialResults(partialResults: Bundle?) = Unit
                override fun onEvent(eventType: Int, params: Bundle?) = Unit
            })
        }
    }

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        }
    }

    LaunchedEffect(shouldRecognize, state, microphonePermission) {
        @Suppress("NAME_SHADOWING")
        val shouldRecognize = shouldRecognize.value ?: return@LaunchedEffect

        when {
            shouldRecognize && !microphonePermission.status.isGranted -> {
                updateState(RequestingPermission)
                microphonePermission.launchPermissionRequest()
            }

            shouldRecognize && (state == Ready || state == Failure) -> {
                updateState(Ready)
                recognizer.startListening(recognizerIntent)
            }

            !shouldRecognize && state is Recognizing -> {
                recognizer.stopListening()
                updateState(Ready)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            recognizer.destroy()
        }
    }

    content()
}

private val expectedErrors = listOf(
    "[[list padding]]",
    "ERROR_NETWORK_TIMEOUT",
    "ERROR_NETWORK",
    "ERROR_AUDIO",
    "ERROR_SERVER",
    "ERROR_CLIENT",
    "ERROR_SPEECH_TIMEOUT",
    "ERROR_NO_MATCH",
    "ERROR_RECOGNIZER_BUSY",
    "ERROR_INSUFFICIENT_PERMISSIONS",
    "ERROR_TOO_MANY_REQUESTS",
    "ERROR_SERVER_DISCONNECTED",
    "ERROR_LANGUAGE_NOT_SUPPORTED",
    "ERROR_LANGUAGE_UNAVAILABLE",
    "ERROR_CANNOT_CHECK_SUPPORT",
    "ERROR_CANNOT_LISTEN_TO_DOWNLOAD_EVENTS",
)
