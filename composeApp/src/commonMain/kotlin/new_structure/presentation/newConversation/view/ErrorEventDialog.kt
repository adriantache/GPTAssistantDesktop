package new_structure.presentation.newConversation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorEventDialog(
    errorMessage: String,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column {
            Text(errorMessage)

            Spacer(Modifier.height(32.dp))

            Button(onClick = onDismiss) {
                Text("Dismiss")
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
