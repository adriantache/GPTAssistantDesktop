package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import settings.AppSettings

object AppColor {
    @Composable
    private fun isDarkTheme(
        settings: AppSettings = AppSettings.getInstance(),
    ): Boolean {
        return settings.forceDarkModeFlow.collectAsState().value || isSystemInDarkTheme()
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
}
