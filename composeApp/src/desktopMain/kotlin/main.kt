import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val windowSize = java.awt.Toolkit.getDefaultToolkit().screenSize
    val height = with(LocalDensity.current) { windowSize.height.toDp() }
    val windowState = rememberWindowState(size = DpSize(width = 800.dp, height = height))

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "GPT Assistant Desktop",
    ) {
        App()
    }
}
