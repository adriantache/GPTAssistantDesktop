package presentation.conversation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.AppColor
import util.Strings.DISMISS_BUTTON

// TODO: let this component manage its own visibility.
@Composable
fun ErrorEventDialog(
    errorMessage: String,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(AppColor.card(), RoundedCornerShape(16.dp)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(errorMessage)

            Spacer(Modifier.height(32.dp))

            Button(onClick = onDismiss) {
                Text(DISMISS_BUTTON)
            }
        }
    }
}

@Composable
@Preview
private fun ErrorEventDialogPreview() {
    ErrorEventDialog(
        errorMessage = "Test error.",
        onDismiss = {}
    )
}
