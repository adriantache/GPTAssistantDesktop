package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// TODO: extract colors for common components to this class as well. (button, text field, text, etc.)

object AppColor {
    @Composable
    fun background(): Color = if (isSystemInDarkTheme()) Color(0xff090804) else Color(0xfff6f7fb)

    @Composable
    fun onBackground(): Color = if (isSystemInDarkTheme()) Color.White else Color.Black

    @Composable
    fun userMessage(): Color = if (isSystemInDarkTheme()) Color(0xff3f36a9) else Color(0xff786def)

    @Composable
    fun onUserMessage(): Color = Color.White

    @Composable
    fun card(): Color = if (isSystemInDarkTheme()) Color(0xff1b180c) else Color.White

    @Composable
    fun onCard(): Color = onBackground()

    @Composable
    fun error(): Color = if (isSystemInDarkTheme()) Color(0xffe57373) else Color(0xffc62828)

    @Composable
    fun dark(): Color = if (isSystemInDarkTheme()) Color.White else Color.Black

    @Composable
    fun light(): Color = if (isSystemInDarkTheme()) Color.Black else Color.White

    @Composable
    fun buttonColors() = ButtonDefaults.buttonColors(
        backgroundColor = userMessage(),
        contentColor = onUserMessage(),
    )

    @Composable
    fun errorButtonColors() = ButtonDefaults.buttonColors(
        backgroundColor = error(),
        contentColor = Color.White,
    )

    @Composable
    fun textFieldColors() = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        textColor = onCard(),
        placeholderColor = onCard(),
        focusedLabelColor = onCard(),
        focusedIndicatorColor = onCard(),
        unfocusedLabelColor = onCard().copy(0.7f),
        unfocusedIndicatorColor = onCard().copy(0.7f),
        cursorColor = onCard(),
    )
}
