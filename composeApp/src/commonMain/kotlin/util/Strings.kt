package util

// TODO: move this to use composeResources if adding localization
object Strings {
    const val HOME_ITEM_NEW_CONVERSATION = "New Conversation"
    const val HOME_ITEM_NEW_IMAGE_GENERATION = "New Image Generation"
    const val HOME_ITEM_CONVERSATION_HISTORY = "Conversation History"

    const val PERSONA_SELECTOR_TITLE = "Select persona:"
    const val PERSONA_SELECTOR_NO_PERSONA = "None"
    const val PERSONA_SELECTOR_NO_PERSONA_SELECTED = "No persona"
    const val PERSONA_SELECTOR_EDIT_PERSONA = "Edit"
    const val PERSONA_SELECTOR_DELETE_PERSONA = "Delete"
    const val PERSONA_SELECTOR_ADD_PERSONA = "Add persona"
    const val PERSONA_SELECTOR_NO_PERSONAS_CTA = "You have no personas, why not add one!"

    const val ADD_PERSONA_TITLE = "Add persona"
    const val EDIT_PERSONA_TITLE = "Edit persona"
    const val ADD_PERSONA_NAME_LABEL = "Persona name"
    const val ADD_PERSONA_DESCRIPTION_LABEL = "Persona instructions"
    const val ADD_PERSONA_SAVE = "Save"

    const val IMAGE_INPUT_TUTORIAL = "Enter your prompt to generate an image"
    const val IMAGE_RESET = "Generate a new image"

    const val CONFIRM_BUTTON = "Confirm"
    const val CANCEL_BUTTON = "Cancel"
    const val SAVE_BUTTON = "Save"
    const val DISMISS_BUTTON = "Dismiss"
    const val SEND_BUTTON = "Send"
    const val DELETE_BUTTON = "Delete"

    const val CONVERSATION_HISTORY_DELETE_TITLE = "Delete conversation"
    const val CONVERSATION_HISTORY_DELETE_TEXT = "Are you sure you want to delete this conversation?"

    const val SETTINGS_DELETE_API_KEY = "Delete your OpenAI API key"
    const val SETTINGS_DELETE_API_KEY_TITLE = "Delete API key"
    const val SETTINGS_DELETE_API_KEY_TEXT = "Are you sure you want to delete your API key?"
    const val SETTINGS_CHANGE_TTS_VOICE_TITLE = "Change TTS voice"
    const val SETTINGS_CHANGE_TTS_VOICE_TEXT = "Change the voice of the TTS engine"

    const val API_KEY_MISSING_TITLE = "Please enter your OpenAI API key to connect to ChatGPT"
    const val API_KEY_MISSING_SUBTITLE =
        "Please note that you will need to set up billing in your OpenAI account to access the ChatGPT API."
    const val API_KEY_MISSING_WEBSITE = "Click here to open the OpenAI website to get a key."

    const val CONVERSATION_STOP_TTS = "Stop TTS output"
    const val CONVERSATION_RESET = "Reset conversation"

    const val INPUT_HINT = "Ask ChatGPT"
    const val INPUT_ENTER_TO_SEND = "Press enter to send"
}
