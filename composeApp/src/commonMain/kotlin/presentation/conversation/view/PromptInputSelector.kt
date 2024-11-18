package presentation.conversation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.baseline_keyboard_24
import gptassistant.composeapp.generated.resources.baseline_mic_24
import org.jetbrains.compose.resources.painterResource
import theme.AppColor

@Composable
fun PromptInputSelector(
    keyboardInputContent: @Composable () -> Unit,
    voiceInputContent: @Composable () -> Unit,
) {
    var inputSelection: PromptSelection? by remember { mutableStateOf(null) }

    AnimatedVisibility(inputSelection == null) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.requiredSize(100.dp)
                    .background(AppColor.accent(), CircleShape)
                    .clip(CircleShape)
                    .clickable { inputSelection = PromptSelection.MICROPHONE },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.requiredSize(24.dp),
                    painter = painterResource(Res.drawable.baseline_mic_24),
                    contentDescription = "Microphone input"
                )
            }

            Spacer(Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .requiredSize(100.dp)
                    .background(AppColor.accent(), CircleShape)
                    .clip(CircleShape)
                    .clickable { inputSelection = PromptSelection.KEYBOARD },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.requiredSize(24.dp),
                    painter = painterResource(Res.drawable.baseline_keyboard_24),
                    contentDescription = "Keyboard input"
                )
            }
        }
    }

    AnimatedVisibility(inputSelection == PromptSelection.KEYBOARD) {
        keyboardInputContent()
    }

    AnimatedVisibility(inputSelection == PromptSelection.MICROPHONE) {
        voiceInputContent()
    }
}

private enum class PromptSelection {
    KEYBOARD, MICROPHONE
}
