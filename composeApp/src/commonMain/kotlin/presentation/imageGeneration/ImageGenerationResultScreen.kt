package presentation.imageGeneration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import presentation.imageGeneration.model.ImageGenerationResultItem
import theme.AppColor
import util.Strings

@Composable
fun ImageGenerationResultScreen(
    imageGenerationItem: ImageGenerationResultItem,
) {
    val clipboard = LocalClipboardManager.current

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
            .background(AppColor.card(), RoundedCornerShape(8.dp))
    ) {
        SelectionContainer {
            Text(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                text = imageGenerationItem.imageInput,
                color = AppColor.onCard(),
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(16.dp))

        imageGenerationItem.image?.let {
            AsyncImage(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .clickable { clipboard.setText(AnnotatedString(imageGenerationItem.image)) },
                contentDescription = imageGenerationItem.imageInput,
                model = imageGenerationItem.image,
            )
        }

        imageGenerationItem.errorMessage?.let {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
                    .clickable { imageGenerationItem.onReset() },
                text = "Error: $it",
                color = AppColor.error(),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = imageGenerationItem.onReset,
        ) {
            Text(Strings.IMAGE_RESET)
        }
    }
}
