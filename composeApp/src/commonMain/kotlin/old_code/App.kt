package old_code

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import new_structure.presentation.NewApp
import old_code.migration.MigrationProcessor
import old_code.view.MainScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.AppColor

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        MigrationProcessor {
            var useNewApp by remember { mutableStateOf(false) }
            var hideNewApp by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                launch {
                    delay(3000)

                    if (!useNewApp) hideNewApp = true
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = AppColor.background(),
            ) {
                if (!useNewApp) {
                    MainScreen()

                    if (!hideNewApp) {
                        Button(
                            modifier = Modifier.requiredHeight(48.dp).requiredWidth(200.dp),
                            onClick = { useNewApp = true }
                        ) {
                            Text("Use new app")
                        }
                    }
                } else {
                    NewApp()
                }
            }
        }
    }
}
