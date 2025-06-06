package platformSpecific.tts

actual fun getTtsHelper(): TtsHelper? {
    return TtsHelperImpl
}
