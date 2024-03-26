package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import settings.AppSettings
import theme.AppColor

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsRow(
    settings: AppSettings = AppSettings.getInstance(),
) {
    var areSettingsOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(areSettingsOpen) {
            Row {
                Button(
                    onClick = {
                        settings.forceDarkMode = !settings.forceDarkMode
                        areSettingsOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColor.userMessage(),
                        contentColor = AppColor.onUserMessage(),
                    )
                ) {
                    val label = if (settings.forceDarkMode) "Clear Dark Mode" else "Force Dark Mode"

                    Text(label)
                }

                Spacer(Modifier.width(16.dp))

                // TODO: extract this component
                Button(
                    onClick = {
                        settings.apiKey = null
                        areSettingsOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColor.userMessage(),
                        contentColor = AppColor.onUserMessage(),
                    )
                ) {
                    Text("Remove API key")
                }

                Spacer(Modifier.width(16.dp))
            }
        }

        Image(
            modifier = Modifier.clickable { areSettingsOpen = !areSettingsOpen }
                .requiredSize(48.dp)
                .padding(8.dp),
            painter = painterResource(Res.drawable.settings),
            contentDescription = null,
            colorFilter = ColorFilter.tint(AppColor.onBackground()),
        )
    }
}