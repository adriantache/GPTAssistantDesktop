package new_structure.presentation.imageGeneration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import new_structure.presentation.imageGeneration.model.ImageGenerationResultItem
import theme.AppColor

@Composable
fun ImageGenerationResultScreen(
    imageGenerationItem: ImageGenerationResultItem,
) {
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

        AsyncImage(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentDescription = imageGenerationItem.imageInput,
            model = imageGenerationItem.image,
        )
    }
}
