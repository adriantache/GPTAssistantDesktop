import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import migration.MigrationProcessor
import new_structure.presentation.NewApp
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.AppColor

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
//                NewApp()  // used for testing until the new structure is complete
            }
        }
    }
}
