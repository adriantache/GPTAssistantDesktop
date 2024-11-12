package old_code.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.persona
import gptassistant.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.painterResource
import theme.AppColor

@Composable
fun SettingsRow(

) {
    var areSettingsOpen by remember { mutableStateOf(false) }
    var showPersonasDialog by remember { mutableStateOf(false) }


    Row(
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .padding(vertical = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(8.dp))

        Button(
            modifier = Modifier.requiredHeight(36.dp),
            onClick = { showPersonasDialog = true },
            colors = AppColor.buttonColors(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.requiredSize(24.dp),
                    painter = painterResource(Res.drawable.persona),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(AppColor.onUserMessage()),
                )

                Spacer(Modifier.width(8.dp))

                Text("settings.selectedPersona?.name" ?: "No persona")
            }
        }

        Spacer(Modifier.weight(1f))

        AnimatedVisibility(areSettingsOpen) {
            Row {
                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = {

                        areSettingsOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColor.userMessage(),
                        contentColor = AppColor.onUserMessage(),
                    )
                ) {
                    val label = "Force Dark Mode"

                    Text(label)
                }

                Spacer(Modifier.width(16.dp))

                // TODO: extract this component or its colours and reuse it
                Button(
                    onClick = {

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
            contentDescription = "Settings",
            colorFilter = ColorFilter.tint(AppColor.onBackground()),
        )
    }

    if (showPersonasDialog) {
        PersonaSelectorDialog(
            onDismiss = { showPersonasDialog = false },
        )
    }
}
