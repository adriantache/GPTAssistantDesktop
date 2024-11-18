package new_structure.presentation.shared

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import new_structure.util.Strings.CANCEL_BUTTON
import new_structure.util.Strings.CONFIRM_BUTTON
import theme.AppColor

@Composable
fun ConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = CONFIRM_BUTTON,
                    color = AppColor.error()
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = CANCEL_BUTTON,
                    color = AppColor.onCard()
                )
            }
        },
        title = { Text(title) },
        text = { Text(text) }
    )
}
