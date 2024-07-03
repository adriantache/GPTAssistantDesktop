import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import coil3.util.Logger
import migration.MigrationProcessor
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.AppColor

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
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = AppColor.background(),
            ) {
                MainScreen()
//                NewApp()  // used for testing until the new structure is complete
            }
        }
    }
}
