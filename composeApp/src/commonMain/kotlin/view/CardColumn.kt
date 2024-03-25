package view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import theme.AppColor

@Composable
fun CardColumn(
    modifier: Modifier = Modifier.fillMaxWidth(),
    color: Color = AppColor.card(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.padding(16.dp),
        color = color,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            this.content()
        }
    }
}