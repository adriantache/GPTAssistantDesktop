package presentation.navigation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.shared.ConfirmationDialog

@Composable
fun SettingsRow(
    text: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onChecked(!checked)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onChecked,
        )
    }
}

@Composable
fun SettingsRow(
    text: String,
    onClick: () -> Unit,
    confirmationTitle: String,
    confirmationText: String,
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showConfirmationDialog = true }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
        )
    }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            onDismiss = { showConfirmationDialog = false },
            onConfirm = onClick,
            title = confirmationTitle,
            text = confirmationText,
        )
    }
}

@Preview
@Composable
fun SettingsRowPreview() {
    SettingsRow(
        text = "test",
        checked = true,
        onChecked = {},
    )
}
