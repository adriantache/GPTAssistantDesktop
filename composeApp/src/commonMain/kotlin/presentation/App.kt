package presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.migration.MigrationProcessor
import presentation.navigation.Navigation
import theme.AppColor

@Composable
fun App() {
    MaterialTheme {
        MigrationProcessor {
            Scaffold { paddingValues ->
                Surface(
                    modifier = Modifier.fillMaxSize().padding(paddingValues).systemBarsPadding(),
                    color = AppColor.background(),
                ) {
                    Navigation()
                }
            }
        }
    }
}
