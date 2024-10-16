package old_code

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import new_structure.presentation.NewApp
import old_code.migration.MigrationProcessor
import old_code.view.MainScreen
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
                if (false) {
                    MainScreen()
                } else {
                    NewApp()
                }
            }
        }
    }
}
