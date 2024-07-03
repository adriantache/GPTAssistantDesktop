import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import coil3.util.Logger
import migration.MigrationProcessor
import new_structure.presentation.NewApp
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.AppColor
import view.MainScreen

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .logger(DebugLogger(minLevel = Logger.Level.Info))
                .crossfade(true)
                .build()
        }

        MigrationProcessor {
            var useNewApp by remember { mutableStateOf(false) }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = AppColor.background(),
            ) {
                if (!useNewApp) {
                    MainScreen()

                    Button(onClick = { useNewApp = true }) {
                        Text("Use new app")
                    }
                } else {
                    NewApp()
                }
            }
        }
    }
}
