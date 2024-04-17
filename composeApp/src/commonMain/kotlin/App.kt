import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import migration.MigrationProcessor
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.AppColor
import view.MainScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        MigrationProcessor {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = AppColor.background(),
            ) {

                MainScreen()
            }
        }
    }
}
