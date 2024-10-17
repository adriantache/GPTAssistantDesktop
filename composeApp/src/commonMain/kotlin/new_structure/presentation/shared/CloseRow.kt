package new_structure.presentation.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// TODO: improve this
@Composable
fun CloseRow(
    onClose: () -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClose() }
            .padding(8.dp)
            .padding(end = 16.dp),
        text = "X",
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End,
    )
}
