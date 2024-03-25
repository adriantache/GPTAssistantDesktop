package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import settings.AppSettings

object AppColor {
    @Composable
    fun isDarkTheme(
        settings: AppSettings = AppSettings.getInstance(),
    ): Boolean {
        return settings.forceDarkMode || isSystemInDarkTheme()
    }

    @Composable
    fun background(): Color = ColorPair(
        lightColor = Color(0xfff6f7fb),
        darkColor = Color(0xff090804),
    ).get(isDarkTheme())

    @Composable
    fun onBackground(): Color = ColorPair(
        lightColor = Color.Black,
        darkColor = Color.White,
    ).get(isDarkTheme())

    @Composable
    fun userMessage(): Color = ColorPair(
        lightColor = Color(0xff786def),
        darkColor = Color(0xff3f36a9),
    ).get(isDarkTheme())

    @Composable
    fun onUserMessage(): Color = ColorPair(
        lightColor = Color.White,
        darkColor = Color.White,
    ).get(isDarkTheme())

    @Composable
    fun card(): Color = ColorPair(
        lightColor = Color.White,
        darkColor = Color(0xff1b180c),
    ).get(isDarkTheme())

    @Composable
    fun onCard(): Color = onBackground()
}

private data class ColorPair(
    val lightColor: Color,
    val darkColor: Color,
) {
    fun get(isDarkMode: Boolean) = if (isDarkMode) darkColor else lightColor
}