package new_structure.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import gptassistant.composeapp.generated.resources.Res
import gptassistant.composeapp.generated.resources.baseline_close_24
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import theme.AppColor

@Composable
fun CloseRow(
    extraItems: List<CloseRowItem>? = null,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().requiredHeight(48.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        extraItems?.forEach {
            CloseRowIcon(it)

            Spacer(Modifier.width(8.dp))
        }

        CloseRowIcon(
            CloseRowItem(
                imageRes = Res.drawable.baseline_close_24,
                contentDescription = "Close",
                onClick = { onClose() }
            )
        )
    }
}

@Composable
private fun CloseRowIcon(item: CloseRowItem) {
    Image(
        modifier = Modifier.clickable { item.onClick() }
            .padding(8.dp),
        painter = painterResource(item.imageRes),
        contentDescription = item.contentDescription,
        colorFilter = ColorFilter.tint(AppColor.onBackground()),
    )
}

data class CloseRowItem(
    val imageRes: DrawableResource,
    val contentDescription: String,
    val onClick: () -> Unit,
)
