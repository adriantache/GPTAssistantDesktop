package presentation.conversation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.baseline_voice_over_off_24
import org.jetbrains.compose.resources.painterResource
import theme.AppColor
import util.Strings.CONVERSATION_STOP_TTS

@Composable
fun StopTtsView(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(AppColor.accent(), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.baseline_voice_over_off_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(AppColor.onAccent())
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = CONVERSATION_STOP_TTS,
            color = AppColor.onAccent()
        )
    }
}
