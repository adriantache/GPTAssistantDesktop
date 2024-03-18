import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val windowState = rememberWindowState(size = DpSize(width = 800.dp, height = Dp.Unspecified))

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "GPT Assistant Desktop",
    ) {
        App()
    }
}