package new_structure.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import new_structure.presentation.navigation.Navigation
import old_code.migration.MigrationProcessor
import theme.AppColor

// TODO: migrate all data

@Composable
fun NewApp() {
    MaterialTheme {
        MigrationProcessor {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = AppColor.background(),
            ) {
                Navigation()
            }
        }
    }
}
