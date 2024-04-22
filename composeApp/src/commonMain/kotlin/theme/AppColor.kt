package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import settings.AppSettings

// TODO: extract colors for common components to this class as well. (button, text field, text, etc.)

object AppColor {
    @Composable
    private fun isDarkTheme(
        settings: AppSettings = AppSettings.getInstance(),
    ): Boolean {
        return settings.forceDarkModeFlow.collectAsState(null).value == true || isSystemInDarkTheme()
    }

    @Composable
    fun background(): Color = if (isDarkTheme()) Color(0xff090804) else Color(0xfff6f7fb)

    @Composable
    fun onBackground(): Color = if (isDarkTheme()) Color.White else Color.Black

    @Composable
    fun userMessage(): Color = if (isDarkTheme()) Color(0xff3f36a9) else Color(0xff786def)

    @Composable
    fun onUserMessage(): Color = Color.White

    @Composable
    fun card(): Color = if (isDarkTheme()) Color(0xff1b180c) else Color.White

    @Composable
    fun onCard(): Color = onBackground()

    @Composable
    fun dark(): Color = if (isDarkTheme()) Color.White else Color.Black

    @Composable
    fun light(): Color = if (isDarkTheme()) Color.Black else Color.White

    @Composable
    fun buttonColors() = ButtonDefaults.buttonColors(
        backgroundColor = userMessage(),
        contentColor = onUserMessage(),
    )
}
